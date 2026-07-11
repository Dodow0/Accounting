package com.dodo.accounting.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dodo.accounting.core.time.addDaysMillis
import com.dodo.accounting.core.time.addMonthsMillis
import com.dodo.accounting.core.time.localDateFromMillis
import com.dodo.accounting.core.time.localDateStartMillis
import com.dodo.accounting.core.time.startOfDayMillis
import com.dodo.accounting.core.time.startOfMonthMillis
import com.dodo.accounting.data.local.model.TransactionWithDetails
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HomeUiState(
    val homeTransactions: List<TransactionWithDetails> = emptyList(),
    val homePeriod: HomePeriod = HomePeriod.MONTH,
    val homeRangeStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
    val homeRangeEndMillis: Long = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1),
    val isLoading: Boolean = true
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AccountingRepository,
    private val addTransaction: AddTransactionUseCase,
    private val messenger: UiMessenger
) : ViewModel() {
    private val homePeriod = MutableStateFlow(HomePeriod.MONTH)
    private val homeAnchorMillis = MutableStateFlow(startOfDayMillis(System.currentTimeMillis()))
    private val homeCustomRange = MutableStateFlow(defaultHomeCustomRange())
    private val editingTransaction = MutableStateFlow<TransactionWithDetails?>(null)

    private val transactionActions by lazy {
        TransactionActions(viewModelScope, repository, addTransaction, editingTransaction, messenger::show)
    }

    private val homeRangeFlow = combine(homePeriod, homeAnchorMillis, homeCustomRange) { period, anchorMillis, customRange ->
        when (period) {
            HomePeriod.WEEK -> {
                val range = StatsPeriod.WEEK.rangeContaining(localDateFromMillis(anchorMillis))
                HomeRangeInternal(period, range.startMillis, range.endMillis)
            }
            HomePeriod.MONTH -> {
                val range = StatsPeriod.MONTH.rangeContaining(localDateFromMillis(anchorMillis))
                HomeRangeInternal(period, range.startMillis, range.endMillis)
            }
            HomePeriod.YEAR -> {
                val range = StatsPeriod.YEAR.rangeContaining(localDateFromMillis(anchorMillis))
                HomeRangeInternal(period, range.startMillis, range.endMillis)
            }
            HomePeriod.CUSTOM -> customRange
        }
    }

    val uiState: StateFlow<HomeUiState> = homeRangeFlow.flatMapLatest { range ->
        repository.searchTransactions(
            query = "",
            startAt = range.startMillis,
            endAt = range.endMillis,
            limit = 1_000
        ).map { transactions ->
            HomeUiState(
                homeTransactions = transactions,
                homePeriod = range.period,
                homeRangeStartMillis = range.startMillis,
                homeRangeEndMillis = range.endMillis,
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun setHomePeriod(period: HomePeriod) {
        homePeriod.value = period
    }

    fun moveHomePeriod(delta: Int) {
        when (homePeriod.value) {
            HomePeriod.WEEK -> {
                homeAnchorMillis.value = movePeriodAnchorMillis(homeAnchorMillis.value, StatsPeriod.WEEK, delta)
            }
            HomePeriod.MONTH -> {
                homeAnchorMillis.value = movePeriodAnchorMillis(homeAnchorMillis.value, StatsPeriod.MONTH, delta)
            }
            HomePeriod.YEAR -> {
                homeAnchorMillis.value = movePeriodAnchorMillis(homeAnchorMillis.value, StatsPeriod.YEAR, delta)
            }
            HomePeriod.CUSTOM -> {
                val range = homeCustomRange.value
                val days = ((range.endMillis - range.startMillis) / DAY_MILLIS).coerceAtLeast(1L).toInt()
                homeCustomRange.value = range.copy(
                    startMillis = addDaysMillis(range.startMillis, days * delta),
                    endMillis = addDaysMillis(range.endMillis, days * delta)
                )
            }
        }
    }

    fun resetHomePeriodToCurrent() {
        homeAnchorMillis.value = startOfDayMillis(System.currentTimeMillis())
        if (homePeriod.value == HomePeriod.CUSTOM) {
            homeCustomRange.value = defaultHomeCustomRange()
        }
    }

    fun setHomeCustomRange(startMillis: Long, endMillis: Long) {
        val start = startOfDayMillis(minOf(startMillis, endMillis))
        val endInclusive = startOfDayMillis(maxOf(startMillis, endMillis))
        homeCustomRange.value = HomeRangeInternal(
            period = HomePeriod.CUSTOM,
            startMillis = start,
            endMillis = addDaysMillis(endInclusive, 1)
        )
        homePeriod.value = HomePeriod.CUSTOM
    }

    fun deleteTransaction(transactionId: Long) = transactionActions.deleteTransaction(transactionId)
}

private data class HomeRangeInternal(
    val period: HomePeriod,
    val startMillis: Long,
    val endMillis: Long
)

private fun defaultHomeCustomRange(): HomeRangeInternal {
    val start = startOfMonthMillis(System.currentTimeMillis())
    return HomeRangeInternal(
        period = HomePeriod.CUSTOM,
        startMillis = start,
        endMillis = addMonthsMillis(start, 1)
    )
}

private const val DAY_MILLIS = 24L * 60L * 60L * 1000L

internal fun movePeriodAnchorMillis(
    millis: Long,
    period: StatsPeriod,
    delta: Int
): Long {
    val date = localDateFromMillis(millis)
    val moved = when (period) {
        StatsPeriod.WEEK -> date.plusWeeks(delta.toLong())
        StatsPeriod.MONTH -> date.plusMonths(delta.toLong())
        StatsPeriod.YEAR -> date.plusYears(delta.toLong())
    }
    return localDateStartMillis(moved)
}
