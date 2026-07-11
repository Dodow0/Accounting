package com.dodo.accounting.ui.viewmodel

import com.dodo.accounting.ui.viewmodel.actions.BackupActions
import com.dodo.accounting.ui.viewmodel.actions.BackupUiLocalState
import com.dodo.accounting.ui.viewmodel.actions.ManagementActions
import com.dodo.accounting.ui.viewmodel.actions.PlanningActions
import com.dodo.accounting.ui.viewmodel.actions.TransactionActions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dodo.accounting.core.time.addDaysMillis
import com.dodo.accounting.core.time.addMonthsMillis
import com.dodo.accounting.core.time.localDateFromMillis
import com.dodo.accounting.core.time.startOfDayMillis
import com.dodo.accounting.core.time.startOfMonthMillis
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import com.dodo.accounting.domain.model.AccountingSummary
import com.dodo.accounting.domain.model.DateRange
import com.dodo.accounting.domain.model.StatsPeriod
import com.dodo.accounting.domain.model.rangeContaining
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class StatsUiState(
    val summary: AccountingSummary? = null,
    val periodTransactions: List<TransactionWithDetails> = emptyList(),
    val calendarMonthTransactions: List<TransactionWithDetails> = emptyList(),
    val trendBuckets: List<TrendSummaryRow> = emptyList(),
    val calendarMonthStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
    val calendarSelectedDateMillis: Long = startOfDayMillis(System.currentTimeMillis()),
    val statsAnchorMillis: Long = startOfDayMillis(System.currentTimeMillis()),
    val statsRangeMode: StatsRangeMode = StatsRangeMode.MONTH,
    val statsRangeStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
    val statsRangeEndMillis: Long = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1),
    val statsRangeLabel: String = "",
    val selectedPeriod: StatsPeriod = StatsPeriod.MONTH,
    val activeAccounts: List<com.dodo.accounting.data.local.entity.AccountEntity> = emptyList(),
    val accounts: List<com.dodo.accounting.data.local.model.AccountBalanceRow> = emptyList(),
    val categories: List<com.dodo.accounting.data.local.entity.CategoryEntity> = emptyList(),
    val tags: List<com.dodo.accounting.data.local.entity.TagEntity> = emptyList(),
    val isLoading: Boolean = true
) {
    val expenseCategories: List<com.dodo.accounting.data.local.entity.CategoryEntity> =
        categories.filter { it.kind == com.dodo.accounting.data.local.entity.CategoryKind.EXPENSE }
    val incomeCategories: List<com.dodo.accounting.data.local.entity.CategoryEntity> =
        categories.filter { it.kind == com.dodo.accounting.data.local.entity.CategoryKind.INCOME }
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: AccountingRepository,
    private val addTransaction: AddTransactionUseCase,
    private val messenger: UiMessenger
) : ViewModel() {
    private val selectedPeriod = MutableStateFlow(StatsPeriod.MONTH)
    private val statsRangeMode = MutableStateFlow(StatsRangeMode.MONTH)
    private val statsAnchorMillis = MutableStateFlow(startOfDayMillis(System.currentTimeMillis()))
    private val statsCustomRange = MutableStateFlow(defaultStatsCustomRange())
    private val calendarMonthStartMillis = MutableStateFlow(startOfMonthMillis(System.currentTimeMillis()))
    private val calendarSelectedDateMillis = MutableStateFlow(startOfDayMillis(System.currentTimeMillis()))
    private val editingTransaction = MutableStateFlow<TransactionWithDetails?>(null)

    private val transactionActions by lazy {
        TransactionActions(viewModelScope, repository, addTransaction, editingTransaction, messenger::show)
    }

    private val statsRangeFlow = combine(
        statsRangeMode,
        selectedPeriod,
        statsAnchorMillis,
        statsCustomRange
    ) { mode, period, anchorMillis, customRange ->
        when (mode) {
            StatsRangeMode.WEEK -> StatsPeriod.WEEK.rangeContaining(localDateFromMillis(anchorMillis)).toStatsRangeState(mode, StatsPeriod.WEEK)
            StatsRangeMode.MONTH -> StatsPeriod.MONTH.rangeContaining(localDateFromMillis(anchorMillis)).toStatsRangeState(mode, StatsPeriod.MONTH)
            StatsRangeMode.YEAR -> StatsPeriod.YEAR.rangeContaining(localDateFromMillis(anchorMillis)).toStatsRangeState(mode, StatsPeriod.YEAR)
            StatsRangeMode.CUSTOM -> StatsRangeInternal(
                mode = StatsRangeMode.CUSTOM,
                period = period,
                startMillis = customRange.startMillis,
                endMillis = customRange.endMillis,
                label = customStatsRangeLabel(customRange.startMillis, customRange.endMillis)
            )
        }
    }

    private val summaryFlow = statsRangeFlow.flatMapLatest { range ->
        combine(
            repository.observePeriodSummary(range.startMillis, range.endMillis),
            repository.observeExpenseByCategory(range.startMillis, range.endMillis)
        ) { totals, expenseByCategory ->
            AccountingSummary(
                periodLabel = range.label,
                period = range.period,
                totals = totals,
                expenseByCategory = expenseByCategory
            )
        }
    }

    private val periodTransactionsFlow = statsRangeFlow.flatMapLatest { range ->
        repository.searchTransactions(
            query = "",
            startAt = range.startMillis,
            endAt = range.endMillis,
            limit = 1_000
        )
    }

    private val calendarMonthTransactionsFlow = calendarMonthStartMillis.flatMapLatest { monthStart ->
        repository.searchTransactions(
            query = "",
            startAt = monthStart,
            endAt = addMonthsMillis(monthStart, 1),
            limit = 1_000
        )
    }

    private val trendBucketsFlow = kotlinx.coroutines.flow.flow {
        val trendMonthStart = startOfMonthMillis(System.currentTimeMillis())
        repository.observeMonthlyTrend(
            startAt = addMonthsMillis(trendMonthStart, -5),
            endAt = addMonthsMillis(trendMonthStart, 1)
        ).collect { emit(it) }
    }

    private val catalogFlow = combine(
        repository.observeActiveAccounts(),
        repository.observeAccountBalances(),
        repository.observeCategories(),
        repository.observeTags()
    ) { activeAccounts, accounts, categories, tags ->
        StatsCatalogBundle(activeAccounts, accounts, categories, tags)
    }

    private val analysisFlow = combine(
        summaryFlow,
        periodTransactionsFlow,
        calendarMonthTransactionsFlow,
        trendBucketsFlow,
        combine(
            statsRangeFlow,
            statsAnchorMillis,
            calendarMonthStartMillis,
            calendarSelectedDateMillis
        ) { range, anchor, monthStart, selectedDate ->
            StatsMeta(
                range = range,
                anchor = anchor,
                monthStart = monthStart,
                selectedDate = selectedDate
            )
        }
    ) { summary, periodTx, calendarTx, trends, meta ->
        StatsAnalysisBundle(summary, periodTx, calendarTx, trends, meta)
    }

    val uiState: StateFlow<StatsUiState> = combine(
        analysisFlow,
        catalogFlow
    ) { analysis, catalog ->
        StatsUiState(
            summary = analysis.summary,
            periodTransactions = analysis.periodTx,
            calendarMonthTransactions = analysis.calendarTx,
            trendBuckets = analysis.trends,
            calendarMonthStartMillis = analysis.meta.monthStart,
            calendarSelectedDateMillis = analysis.meta.selectedDate,
            statsAnchorMillis = analysis.meta.anchor,
            statsRangeMode = analysis.meta.range.mode,
            statsRangeStartMillis = analysis.meta.range.startMillis,
            statsRangeEndMillis = analysis.meta.range.endMillis,
            statsRangeLabel = analysis.meta.range.label,
            selectedPeriod = analysis.meta.range.period,
            activeAccounts = catalog.activeAccounts,
            accounts = catalog.accounts,
            categories = catalog.categories,
            tags = catalog.tags,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StatsUiState()
    )

    fun setPeriod(period: StatsPeriod) {
        selectedPeriod.value = period
        statsRangeMode.value = period.toStatsRangeMode()
    }

    fun setStatsRangeMode(mode: StatsRangeMode) {
        when (mode) {
            StatsRangeMode.WEEK -> setPeriod(StatsPeriod.WEEK)
            StatsRangeMode.MONTH -> setPeriod(StatsPeriod.MONTH)
            StatsRangeMode.YEAR -> setPeriod(StatsPeriod.YEAR)
            StatsRangeMode.CUSTOM -> statsRangeMode.value = StatsRangeMode.CUSTOM
        }
    }

    fun moveStatsPeriod(delta: Int) {
        if (statsRangeMode.value == StatsRangeMode.CUSTOM) {
            val range = statsCustomRange.value
            val days = ((range.endMillis - range.startMillis)  / (24L * 60L * 60L * 1000L)).coerceAtLeast(1L).toInt()
            statsCustomRange.value = range.copy(
                startMillis = addDaysMillis(range.startMillis, days * delta),
                endMillis = addDaysMillis(range.endMillis, days * delta)
            )
        } else {
            statsAnchorMillis.value = movePeriodAnchorMillis(
                millis = statsAnchorMillis.value,
                period = selectedPeriod.value,
                delta = delta
            )
        }
    }

    fun resetStatsPeriod() {
        statsAnchorMillis.value = startOfDayMillis(System.currentTimeMillis())
        if (statsRangeMode.value == StatsRangeMode.CUSTOM) {
            statsCustomRange.value = defaultStatsCustomRange()
        }
    }

    fun setStatsCustomRange(startMillis: Long, endMillis: Long) {
        val start = startOfDayMillis(minOf(startMillis, endMillis))
        val endInclusive = startOfDayMillis(maxOf(startMillis, endMillis))
        statsCustomRange.value = StatsCustomRangeInternal(
            startMillis = start,
            endMillis = addDaysMillis(endInclusive, 1)
        )
        statsRangeMode.value = StatsRangeMode.CUSTOM
    }

    fun moveCalendarMonth(deltaMonths: Int) {
        val nextMonth = addMonthsMillis(calendarMonthStartMillis.value, deltaMonths)
        calendarMonthStartMillis.value = nextMonth
        calendarSelectedDateMillis.value = nextMonth
    }

    fun selectCalendarDate(millis: Long) {
        calendarSelectedDateMillis.value = startOfDayMillis(millis)
    }

    fun deleteTransaction(transactionId: Long) = transactionActions.deleteTransaction(transactionId)
}




private data class StatsCatalogBundle(
    val activeAccounts: List<com.dodo.accounting.data.local.entity.AccountEntity>,
    val accounts: List<com.dodo.accounting.data.local.model.AccountBalanceRow>,
    val categories: List<com.dodo.accounting.data.local.entity.CategoryEntity>,
    val tags: List<com.dodo.accounting.data.local.entity.TagEntity>
)

private data class StatsAnalysisBundle(
    val summary: AccountingSummary,
    val periodTx: List<TransactionWithDetails>,
    val calendarTx: List<TransactionWithDetails>,
    val trends: List<TrendSummaryRow>,
    val meta: StatsMeta
)

private data class StatsRangeInternal(
    val mode: StatsRangeMode,
    val period: StatsPeriod,
    val startMillis: Long,
    val endMillis: Long,
    val label: String
)

private data class StatsCustomRangeInternal(
    val startMillis: Long,
    val endMillis: Long
)

private data class StatsMeta(
    val range: StatsRangeInternal,
    val anchor: Long,
    val monthStart: Long,
    val selectedDate: Long
)

private fun DateRange.toStatsRangeState(
    mode: StatsRangeMode,
    period: StatsPeriod
): StatsRangeInternal {
    return StatsRangeInternal(
        mode = mode,
        period = period,
        startMillis = startMillis,
        endMillis = endMillis,
        label = label
    )
}

private fun StatsPeriod.toStatsRangeMode(): StatsRangeMode = when (this) {
    StatsPeriod.WEEK -> StatsRangeMode.WEEK
    StatsPeriod.MONTH -> StatsRangeMode.MONTH
    StatsPeriod.YEAR -> StatsRangeMode.YEAR
}

private fun defaultStatsCustomRange(): StatsCustomRangeInternal {
    val start = startOfMonthMillis(System.currentTimeMillis())
    return StatsCustomRangeInternal(
        startMillis = start,
        endMillis = addMonthsMillis(start, 1)
    )
}

private fun customStatsRangeLabel(startMillis: Long, endMillis: Long): String {
    val start = localDateFromMillis(startMillis)
    val endInclusive = localDateFromMillis(addDaysMillis(endMillis, -1))
    return if (start == endInclusive) {
        "${start.year}-${start.monthValue.toString().padStart(2, '0')}-${start.dayOfMonth.toString().padStart(2, '0')}"
    } else {
        val startLabel = "${start.year}-${start.monthValue.toString().padStart(2, '0')}-${start.dayOfMonth.toString().padStart(2, '0')}"
        val endLabel = "${endInclusive.year}-${endInclusive.monthValue.toString().padStart(2, '0')}-${endInclusive.dayOfMonth.toString().padStart(2, '0')}"
        "$startLabel - $endLabel"
    }
}
