package com.dodo.accounting.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.AccountType
import com.dodo.accounting.data.local.entity.BudgetEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import com.dodo.accounting.domain.model.AccountingSummary
import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.domain.model.DateRange
import com.dodo.accounting.domain.model.StatsPeriod
import com.dodo.accounting.domain.model.rangeContaining
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.usecase.AddTransactionUseCase
import com.dodo.accounting.domain.usecase.EnsureSeedDataUseCase
import com.dodo.accounting.domain.usecase.ExportBackupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.Calendar
import javax.inject.Inject

data class AccountingUiState(
    val accounts: List<AccountBalanceRow> = emptyList(),
    val activeAccounts: List<AccountEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val tags: List<TagEntity> = emptyList(),
    val recentTransactions: List<TransactionWithDetails> = emptyList(),
    val activeTransactionCount: Int = 0,
    val searchResults: List<TransactionWithDetails> = emptyList(),
    val trash: List<TransactionWithDetails> = emptyList(),
    val summary: AccountingSummary? = null,
    val monthlyBudget: BudgetEntity? = null,
    val categoryBudgets: List<BudgetEntity> = emptyList(),
    val monthlyExpenseByCategory: List<CategorySummaryRow> = emptyList(),
    val entryMonthExpenseByCategory: List<CategorySummaryRow> = emptyList(),
    val recurringRules: List<RecurringRuleEntity> = emptyList(),
    val homeTransactions: List<TransactionWithDetails> = emptyList(),
    val homePeriod: HomePeriod = HomePeriod.MONTH,
    val homeRangeStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
    val homeRangeEndMillis: Long = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1),
    val calendarMonthTransactions: List<TransactionWithDetails> = emptyList(),
    val periodTransactions: List<TransactionWithDetails> = emptyList(),
    val trendBuckets: List<TrendSummaryRow> = emptyList(),
    val calendarMonthStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
    val calendarSelectedDateMillis: Long = startOfDayMillis(System.currentTimeMillis()),
    val statsAnchorMillis: Long = startOfDayMillis(System.currentTimeMillis()),
    val statsRangeMode: StatsRangeMode = StatsRangeMode.MONTH,
    val statsRangeStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
    val statsRangeEndMillis: Long = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1),
    val statsRangeLabel: String = "",
    val editingTransaction: TransactionWithDetails? = null,
    val selectedPeriod: StatsPeriod = StatsPeriod.MONTH,
    val searchQuery: String = "",
    val searchType: TransactionType? = null,
    val selectedAccountId: Long? = null,
    val exportPreview: String = "",
    val exportContent: String = "",
    val exportFormat: ExportFormat = ExportFormat.JSON,
    val pendingImportPreview: BackupPreview? = null,
    val pendingImportContent: String = "",
    val isLoading: Boolean = true,
    val message: String? = null
) {
    val totalAssetsCents: Long = accounts
        .filterNot { it.account.isArchived }
        .sumOf { it.balanceCents }

    val expenseCategories: List<CategoryEntity> = categories.filter { it.kind == CategoryKind.EXPENSE }
    val incomeCategories: List<CategoryEntity> = categories.filter { it.kind == CategoryKind.INCOME }
}

enum class ExportFormat {
    JSON,
    CSV
}

enum class HomePeriod {
    WEEK,
    MONTH,
    YEAR,
    CUSTOM
}

enum class StatsRangeMode {
    WEEK,
    MONTH,
    YEAR,
    CUSTOM
}

private data class HomeRangeState(
    val period: HomePeriod,
    val startMillis: Long,
    val endMillis: Long
)

private data class StatsCustomRangeState(
    val startMillis: Long,
    val endMillis: Long
)

private data class StatsRangeState(
    val mode: StatsRangeMode,
    val period: StatsPeriod,
    val startMillis: Long,
    val endMillis: Long,
    val label: String
)

private data class HomeOverviewState(
    val period: HomePeriod,
    val startMillis: Long,
    val endMillis: Long,
    val transactions: List<TransactionWithDetails>
)

private data class CalendarHomeState(
    val calendarMonthTransactions: List<TransactionWithDetails>,
    val homeOverview: HomeOverviewState
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AccountingViewModel @Inject constructor(
    private val repository: AccountingRepository,
    private val ensureSeedData: EnsureSeedDataUseCase,
    private val addTransaction: AddTransactionUseCase,
    private val exportBackup: ExportBackupUseCase
) : ViewModel() {
    private val selectedPeriod = MutableStateFlow(StatsPeriod.MONTH)
    private val statsRangeMode = MutableStateFlow(StatsRangeMode.MONTH)
    private val searchQuery = MutableStateFlow("")
    private val searchType = MutableStateFlow<TransactionType?>(null)
    private val selectedAccountId = MutableStateFlow<Long?>(null)
    private val statsAnchorMillis = MutableStateFlow(startOfDayMillis(System.currentTimeMillis()))
    private val statsCustomRange = MutableStateFlow(defaultStatsCustomRange())
    private val homePeriod = MutableStateFlow(HomePeriod.MONTH)
    private val homeAnchorMillis = MutableStateFlow(startOfDayMillis(System.currentTimeMillis()))
    private val homeCustomRange = MutableStateFlow(defaultHomeCustomRange())
    private val calendarMonthStartMillis = MutableStateFlow(startOfMonthMillis(System.currentTimeMillis()))
    private val entryMonthStartMillis = MutableStateFlow(startOfMonthMillis(System.currentTimeMillis()))
    private val trendDataEnabled = MutableStateFlow(false)
    private val localState = MutableStateFlow(
        AccountingUiState(isLoading = true)
    )
    private val transactionActions by lazy {
        TransactionActions(viewModelScope, repository, addTransaction, localState, ::showMessage)
    }
    private val planningActions by lazy {
        PlanningActions(viewModelScope, repository, ::showMessage)
    }
    private val managementActions by lazy {
        ManagementActions(viewModelScope, repository, ::showMessage)
    }
    private val backupActions by lazy {
        BackupActions(viewModelScope, repository, exportBackup, localState, ::showMessage)
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
            StatsRangeMode.CUSTOM -> StatsRangeState(
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

    private val searchFlow = combine(searchQuery, searchType, selectedAccountId) { query, type, accountId ->
        SearchFilters(query = query, type = type, accountId = accountId)
    }.flatMapLatest { filters ->
        repository.searchTransactions(
            query = filters.query.trim(),
            type = filters.type,
            accountId = filters.accountId
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

    private val homeRangeFlow = combine(homePeriod, homeAnchorMillis, homeCustomRange) { period, anchorMillis, customRange ->
        when (period) {
            HomePeriod.WEEK -> {
                val range = StatsPeriod.WEEK.rangeContaining(localDateFromMillis(anchorMillis))
                HomeRangeState(period, range.startMillis, range.endMillis)
            }
            HomePeriod.MONTH -> {
                val range = StatsPeriod.MONTH.rangeContaining(localDateFromMillis(anchorMillis))
                HomeRangeState(period, range.startMillis, range.endMillis)
            }
            HomePeriod.YEAR -> {
                val range = StatsPeriod.YEAR.rangeContaining(localDateFromMillis(anchorMillis))
                HomeRangeState(period, range.startMillis, range.endMillis)
            }
            HomePeriod.CUSTOM -> customRange
        }
    }

    private val homeOverviewFlow = homeRangeFlow.flatMapLatest { range ->
        repository.searchTransactions(
            query = "",
            startAt = range.startMillis,
            endAt = range.endMillis,
            limit = 1_000
        ).map { transactions ->
            HomeOverviewState(
                period = range.period,
                startMillis = range.startMillis,
                endMillis = range.endMillis,
                transactions = transactions
            )
        }
    }

    private val currentMonthExpenseByCategoryFlow =
        repository.observeExpenseByCategory(
            startAt = startOfMonthMillis(System.currentTimeMillis()),
            endAt = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1)
        )

    private val entryMonthExpenseByCategoryFlow = entryMonthStartMillis.flatMapLatest { monthStart ->
        repository.observeExpenseByCategory(
            startAt = monthStart,
            endAt = addMonthsMillis(monthStart, 1)
        )
    }

    private val categoryExpenseState = combine(
        currentMonthExpenseByCategoryFlow,
        entryMonthExpenseByCategoryFlow
    ) { monthlyExpenseByCategory, entryMonthExpenseByCategory ->
        CategoryExpenseState(
            monthlyExpenseByCategory = monthlyExpenseByCategory,
            entryMonthExpenseByCategory = entryMonthExpenseByCategory
        )
    }

    private val periodTransactionsFlow = statsRangeFlow.flatMapLatest { range ->
        repository.searchTransactions(
            query = "",
            startAt = range.startMillis,
            endAt = range.endMillis,
            limit = 1_000
        )
    }

    private val trendBucketsFlow = trendDataEnabled.flatMapLatest { enabled ->
        if (!enabled) {
            flowOf(emptyList())
        } else {
            val trendMonthStart = startOfMonthMillis(System.currentTimeMillis())
            repository.observeMonthlyTrend(
                startAt = addMonthsMillis(trendMonthStart, -5),
                endAt = addMonthsMillis(trendMonthStart, 1)
            )
        }
    }

    private val transactionBuckets = combine(
        periodTransactionsFlow,
        trendBucketsFlow
    ) { periodTransactions, trendBuckets ->
        TransactionBuckets(
            periodTransactions = periodTransactions,
            trendBuckets = trendBuckets
        )
    }

    private val baseDataState = combine(
        repository.observeAccountBalances(),
        repository.observeActiveAccounts(),
        repository.observeCategories(),
        repository.observeTags(),
        repository.observeRecentTransactions()
    ) { accounts, activeAccounts, categories, tags, recentTransactions ->
        DataState(
            accounts = accounts,
            activeAccounts = activeAccounts,
            categories = categories,
            tags = tags,
            recentTransactions = recentTransactions
        )
    }

    private val dataState = combine(
        baseDataState,
        repository.observeActiveTransactionCount()
    ) { data, activeTransactionCount ->
        data.copy(activeTransactionCount = activeTransactionCount)
    }

    private val planningState = combine(
        repository.observeMonthlyBudget(),
        repository.observeRecurringRules(),
        repository.observeActiveBudgets()
    ) { monthlyBudget, recurringRules, activeBudgets ->
        PlanningState(
            monthlyBudget = monthlyBudget,
            recurringRules = recurringRules,
            categoryBudgets = activeBudgets.filter { it.categoryId != null }
        )
    }

    private val calendarHomeState = combine(
        calendarMonthTransactionsFlow,
        homeOverviewFlow
    ) { calendarMonthTransactions, homeOverview ->
        CalendarHomeState(
            calendarMonthTransactions = calendarMonthTransactions,
            homeOverview = homeOverview
        )
    }

    private val screenDataState = combine(
        dataState,
        planningState,
        calendarHomeState,
        categoryExpenseState,
        transactionBuckets
    ) { data, planning, calendarHome, categoryExpense, buckets ->
        data.copy(
            monthlyBudget = planning.monthlyBudget,
            recurringRules = planning.recurringRules,
            categoryBudgets = planning.categoryBudgets,
            homeTransactions = calendarHome.homeOverview.transactions,
            homePeriod = calendarHome.homeOverview.period,
            homeRangeStartMillis = calendarHome.homeOverview.startMillis,
            homeRangeEndMillis = calendarHome.homeOverview.endMillis,
            calendarMonthTransactions = calendarHome.calendarMonthTransactions,
            monthlyExpenseByCategory = categoryExpense.monthlyExpenseByCategory,
            entryMonthExpenseByCategory = categoryExpense.entryMonthExpenseByCategory,
            periodTransactions = buckets.periodTransactions,
            trendBuckets = buckets.trendBuckets
        )
    }

    private val filterState = combine(
        statsRangeFlow,
        statsAnchorMillis,
        searchQuery,
        searchType,
        selectedAccountId
    ) { statsRange, statsAnchorMillis, query, type, accountId ->
        FilterState(
            period = statsRange.period,
            statsAnchorMillis = statsAnchorMillis,
            statsRangeMode = statsRange.mode,
            statsRangeStartMillis = statsRange.startMillis,
            statsRangeEndMillis = statsRange.endMillis,
            statsRangeLabel = statsRange.label,
            query = query,
            type = type,
            accountId = accountId
        )
    }

    private val screenState = combine(
        screenDataState,
        searchFlow,
        repository.observeTrash(),
        summaryFlow,
        filterState
    ) { data, searchResults, trash, summary, filters ->
        AccountingUiState(
            accounts = data.accounts,
            activeAccounts = data.activeAccounts,
            categories = data.categories,
            tags = data.tags,
            recentTransactions = data.recentTransactions,
            activeTransactionCount = data.activeTransactionCount,
            searchResults = searchResults,
            trash = trash,
            summary = summary,
            monthlyBudget = data.monthlyBudget,
            categoryBudgets = data.categoryBudgets,
            monthlyExpenseByCategory = data.monthlyExpenseByCategory,
            entryMonthExpenseByCategory = data.entryMonthExpenseByCategory,
            recurringRules = data.recurringRules,
            homeTransactions = data.homeTransactions,
            homePeriod = data.homePeriod,
            homeRangeStartMillis = data.homeRangeStartMillis,
            homeRangeEndMillis = data.homeRangeEndMillis,
            calendarMonthTransactions = data.calendarMonthTransactions,
            periodTransactions = data.periodTransactions,
            trendBuckets = data.trendBuckets,
            selectedPeriod = filters.period,
            statsAnchorMillis = filters.statsAnchorMillis,
            statsRangeMode = filters.statsRangeMode,
            statsRangeStartMillis = filters.statsRangeStartMillis,
            statsRangeEndMillis = filters.statsRangeEndMillis,
            statsRangeLabel = filters.statsRangeLabel,
            searchQuery = filters.query,
            searchType = filters.type,
            selectedAccountId = filters.accountId,
            isLoading = false
        )
    }

    val uiState: StateFlow<AccountingUiState> = combine(
        screenState,
        localState
    ) { screen, local ->
        screen.copy(
            exportPreview = local.exportPreview,
            exportContent = local.exportContent,
            exportFormat = local.exportFormat,
            pendingImportPreview = local.pendingImportPreview,
            pendingImportContent = local.pendingImportContent,
            editingTransaction = local.editingTransaction,
            calendarMonthStartMillis = local.calendarMonthStartMillis,
            calendarSelectedDateMillis = local.calendarSelectedDateMillis,
            message = local.message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AccountingUiState()
    )

    init {
        viewModelScope.launch {
            runCatching {
                ensureSeedData()
                repository.generateDueRecurringTransactions()
            }
                .onSuccess { result ->
                    if (result.skippedCount > 0) {
                        showMessage("周期账单已自动生成 ${result.generatedCount} 条，已跳过 ${result.skippedCount} 条超出上限的过期账单")
                    }
                }
                .onFailure { showMessage(it.message ?: "初始化默认数据失败") }
        }
    }

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
            val days = ((range.endMillis - range.startMillis) / DAY_MILLIS).coerceAtLeast(1L).toInt()
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
        statsCustomRange.value = StatsCustomRangeState(
            startMillis = start,
            endMillis = addDaysMillis(endInclusive, 1)
        )
        statsRangeMode.value = StatsRangeMode.CUSTOM
    }

    fun setTrendDataEnabled(enabled: Boolean) {
        trendDataEnabled.value = enabled
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setSearchType(type: TransactionType?) {
        searchType.value = type
    }

    fun setSelectedAccount(accountId: Long?) {
        selectedAccountId.value = accountId
    }

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
        homeCustomRange.value = HomeRangeState(
            period = HomePeriod.CUSTOM,
            startMillis = start,
            endMillis = addDaysMillis(endInclusive, 1)
        )
        homePeriod.value = HomePeriod.CUSTOM
    }

    fun moveCalendarMonth(deltaMonths: Int) {
        val nextMonth = addMonthsMillis(calendarMonthStartMillis.value, deltaMonths)
        calendarMonthStartMillis.value = nextMonth
        localState.update {
            it.copy(
                calendarMonthStartMillis = nextMonth,
                calendarSelectedDateMillis = nextMonth
            )
        }
    }

    fun selectCalendarDate(millis: Long) {
        localState.update { it.copy(calendarSelectedDateMillis = startOfDayMillis(millis)) }
    }

    fun resetCalendarToToday() {
        val now = System.currentTimeMillis()
        val monthStart = startOfMonthMillis(now)
        calendarMonthStartMillis.value = monthStart
        localState.update {
            it.copy(
                calendarMonthStartMillis = monthStart,
                calendarSelectedDateMillis = startOfDayMillis(now)
            )
        }
    }

    fun setEntryOccurredAt(millis: Long) {
        entryMonthStartMillis.value = startOfMonthMillis(millis)
    }

    fun addExpense(
        amount: String,
        accountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) = transactionActions.addExpense(amount, accountId, categoryId, merchant, note, tagIds, occurredAt)

    fun addIncome(
        amount: String,
        accountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) = transactionActions.addIncome(amount, accountId, categoryId, merchant, note, tagIds, occurredAt)

    fun addTransfer(
        amount: String,
        fromAccountId: Long?,
        toAccountId: Long?,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) = transactionActions.addTransfer(amount, fromAccountId, toAccountId, note, tagIds, occurredAt)

    fun startEditTransaction(transaction: TransactionWithDetails) = transactionActions.startEditTransaction(transaction)

    fun cancelEditTransaction() = transactionActions.cancelEditTransaction()

    fun saveEditedTransaction(
        transactionId: Long,
        type: TransactionType,
        amount: String,
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long
    ) = transactionActions.saveEditedTransaction(
        transactionId = transactionId,
        type = type,
        amount = amount,
        accountId = accountId,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        categoryId = categoryId,
        merchant = merchant,
        note = note,
        tagIds = tagIds,
        occurredAt = occurredAt
    )

    suspend fun addEntryTransaction(
        type: TransactionType,
        amount: String,
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long
    ): Result<Unit> = transactionActions.addEntryTransaction(
        type = type,
        amount = amount,
        accountId = accountId,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        categoryId = categoryId,
        merchant = merchant,
        note = note,
        tagIds = tagIds,
        occurredAt = occurredAt
    )

    suspend fun saveEditedTransactionAwait(
        transactionId: Long,
        type: TransactionType,
        amount: String,
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long
    ): Result<Unit> = transactionActions.saveEditedTransactionAwait(
        transactionId = transactionId,
        type = type,
        amount = amount,
        accountId = accountId,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        categoryId = categoryId,
        merchant = merchant,
        note = note,
        tagIds = tagIds,
        occurredAt = occurredAt
    )

    fun addAccount(name: String, type: AccountType, initialBalance: String) =
        managementActions.addAccount(name, type, initialBalance)

    fun updateAccount(
        id: Long,
        name: String,
        type: AccountType,
        initialBalance: String,
        iconName: String,
        colorArgb: Long
    ) = managementActions.updateAccount(
        id = id,
        name = name,
        type = type,
        initialBalance = initialBalance,
        iconName = iconName,
        colorArgb = colorArgb
    )

    fun archiveAccount(accountId: Long) = managementActions.archiveAccount(accountId)

    fun restoreAccount(accountId: Long) = managementActions.restoreAccount(accountId)

    fun deleteAccount(accountId: Long) = managementActions.deleteAccount(accountId)

    fun deleteTransaction(transactionId: Long) = transactionActions.deleteTransaction(transactionId)

    fun restoreTransaction(transactionId: Long) = transactionActions.restoreTransaction(transactionId)

    fun permanentlyDeleteTransaction(transactionId: Long) = transactionActions.permanentlyDeleteTransaction(transactionId)

    fun clearTrash() = transactionActions.clearTrash()

    fun moveAllTransactionsToTrash() = transactionActions.moveAllTransactionsToTrash()

    fun setMonthlyBudget(amount: String) = planningActions.setMonthlyBudget(amount)

    fun setCategoryBudget(category: CategoryEntity, amount: String) = planningActions.setCategoryBudget(category, amount)

    fun addMonthlyRecurringRule(
        name: String,
        type: TransactionType,
        amount: String,
        occurredAt: Long = System.currentTimeMillis(),
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String
    ) = planningActions.addMonthlyRecurringRule(
        name = name,
        type = type,
        amount = amount,
        occurredAt = occurredAt,
        accountId = accountId,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        categoryId = categoryId,
        merchant = merchant,
        note = note
    )

    fun runDueRecurringRules() = planningActions.runDueRecurringRules()

    fun setRecurringRuleEnabled(id: Long, enabled: Boolean) = planningActions.setRecurringRuleEnabled(id, enabled)

    fun deleteRecurringRule(id: Long) = planningActions.deleteRecurringRule(id)

    fun addTag(name: String) = managementActions.addTag(name)

    fun addCategory(
        name: String,
        kind: CategoryKind,
        iconName: String = if (kind == CategoryKind.EXPENSE) "receipt_long" else "work",
        colorArgb: Long = if (kind == CategoryKind.EXPENSE) 0xFFEA580C else 0xFF16A34A
    ) = managementActions.addCategory(name, kind, iconName, colorArgb)

    fun renameCategory(id: Long, name: String) = managementActions.renameCategory(id, name)

    fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) =
        managementActions.updateCategory(id, name, iconName, colorArgb)

    fun moveCategory(id: Long, direction: Int) = managementActions.moveCategory(id, direction)

    fun deleteCategory(id: Long) = managementActions.deleteCategory(id)

    fun renameTag(id: Long, name: String) = managementActions.renameTag(id, name)

    fun deleteTag(id: Long) = managementActions.deleteTag(id)

    fun export(format: ExportFormat) = backupActions.export(format)

    suspend fun exportJsonContent(): Result<String> = runCatching { exportBackup.json() }

    fun importJson(content: String) = backupActions.previewImportJson(content)

    fun confirmImportJson() = backupActions.confirmImportJson()

    fun cancelImportJson() = backupActions.cancelImportJson()

    fun clearMessage() {
        localState.update { it.copy(message = null) }
    }

    private fun showMessage(message: String) {
        localState.update { it.copy(message = message) }
    }

    private data class SearchFilters(
        val query: String,
        val type: TransactionType?,
        val accountId: Long?
    )

    private data class DataState(
        val accounts: List<AccountBalanceRow>,
        val activeAccounts: List<AccountEntity>,
        val categories: List<CategoryEntity>,
        val tags: List<TagEntity>,
        val recentTransactions: List<TransactionWithDetails>,
        val activeTransactionCount: Int = 0,
        val monthlyBudget: BudgetEntity? = null,
        val categoryBudgets: List<BudgetEntity> = emptyList(),
        val recurringRules: List<RecurringRuleEntity> = emptyList(),
        val monthlyExpenseByCategory: List<CategorySummaryRow> = emptyList(),
        val entryMonthExpenseByCategory: List<CategorySummaryRow> = emptyList(),
        val homeTransactions: List<TransactionWithDetails> = emptyList(),
        val homePeriod: HomePeriod = HomePeriod.MONTH,
        val homeRangeStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
        val homeRangeEndMillis: Long = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1),
        val calendarMonthTransactions: List<TransactionWithDetails> = emptyList(),
        val periodTransactions: List<TransactionWithDetails> = emptyList(),
        val trendBuckets: List<TrendSummaryRow> = emptyList()
    )

    private data class TransactionBuckets(
        val periodTransactions: List<TransactionWithDetails>,
        val trendBuckets: List<TrendSummaryRow>
    )

    private data class PlanningState(
        val monthlyBudget: BudgetEntity?,
        val recurringRules: List<RecurringRuleEntity>,
        val categoryBudgets: List<BudgetEntity>
    )

    private data class CategoryExpenseState(
        val monthlyExpenseByCategory: List<CategorySummaryRow>,
        val entryMonthExpenseByCategory: List<CategorySummaryRow>
    )

    private data class FilterState(
        val period: StatsPeriod,
        val statsAnchorMillis: Long,
        val statsRangeMode: StatsRangeMode,
        val statsRangeStartMillis: Long,
        val statsRangeEndMillis: Long,
        val statsRangeLabel: String,
        val query: String,
        val type: TransactionType?,
        val accountId: Long?
    )
}

private fun localDateFromMillis(millis: Long): java.time.LocalDate {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
}

private fun DateRange.toStatsRangeState(
    mode: StatsRangeMode,
    period: StatsPeriod
): StatsRangeState {
    return StatsRangeState(
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

private fun movePeriodAnchorMillis(
    millis: Long,
    period: StatsPeriod,
    delta: Int
): Long {
    return Calendar.getInstance().apply {
        timeInMillis = millis
        when (period) {
            StatsPeriod.WEEK -> add(Calendar.WEEK_OF_YEAR, delta)
            StatsPeriod.MONTH -> add(Calendar.MONTH, delta)
            StatsPeriod.YEAR -> add(Calendar.YEAR, delta)
        }
    }.timeInMillis.let(::startOfDayMillis)
}

private const val DAY_MILLIS = 24L * 60L * 60L * 1000L

private fun defaultHomeCustomRange(): HomeRangeState {
    val start = startOfMonthMillis(System.currentTimeMillis())
    return HomeRangeState(
        period = HomePeriod.CUSTOM,
        startMillis = start,
        endMillis = addMonthsMillis(start, 1)
    )
}

private fun defaultStatsCustomRange(): StatsCustomRangeState {
    val start = startOfMonthMillis(System.currentTimeMillis())
    return StatsCustomRangeState(
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

private fun startOfDayMillis(millis: Long): Long {
    return Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun addDaysMillis(millis: Long, deltaDays: Int): Long {
    return Calendar.getInstance().apply {
        timeInMillis = millis
        add(Calendar.DAY_OF_YEAR, deltaDays)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun startOfMonthMillis(millis: Long): Long {
    return Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun addMonthsMillis(millis: Long, deltaMonths: Int): Long {
    return Calendar.getInstance().apply {
        timeInMillis = millis
        add(Calendar.MONTH, deltaMonths)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
