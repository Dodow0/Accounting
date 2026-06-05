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
import com.dodo.accounting.domain.model.AccountingSummary
import com.dodo.accounting.domain.model.StatsPeriod
import com.dodo.accounting.domain.model.rangeContaining
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.usecase.AddTransactionUseCase
import com.dodo.accounting.domain.usecase.EnsureSeedDataUseCase
import com.dodo.accounting.domain.usecase.ExportBackupUseCase
import com.dodo.accounting.domain.usecase.ObserveAccountingSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class AccountingUiState(
    val accounts: List<AccountBalanceRow> = emptyList(),
    val activeAccounts: List<AccountEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val tags: List<TagEntity> = emptyList(),
    val recentTransactions: List<TransactionWithDetails> = emptyList(),
    val searchResults: List<TransactionWithDetails> = emptyList(),
    val trash: List<TransactionWithDetails> = emptyList(),
    val summary: AccountingSummary? = null,
    val monthlyBudget: BudgetEntity? = null,
    val categoryBudgets: List<BudgetEntity> = emptyList(),
    val monthlyExpenseByCategory: List<CategorySummaryRow> = emptyList(),
    val recurringRules: List<RecurringRuleEntity> = emptyList(),
    val calendarMonthTransactions: List<TransactionWithDetails> = emptyList(),
    val periodTransactions: List<TransactionWithDetails> = emptyList(),
    val trendTransactions: List<TransactionWithDetails> = emptyList(),
    val calendarMonthStartMillis: Long = startOfMonthMillis(System.currentTimeMillis()),
    val calendarSelectedDateMillis: Long = startOfDayMillis(System.currentTimeMillis()),
    val editingTransaction: TransactionWithDetails? = null,
    val selectedPeriod: StatsPeriod = StatsPeriod.MONTH,
    val searchQuery: String = "",
    val searchType: TransactionType? = null,
    val selectedAccountId: Long? = null,
    val exportPreview: String = "",
    val exportContent: String = "",
    val exportFormat: ExportFormat = ExportFormat.JSON,
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

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AccountingViewModel @Inject constructor(
    private val repository: AccountingRepository,
    private val ensureSeedData: EnsureSeedDataUseCase,
    private val addTransaction: AddTransactionUseCase,
    private val observeAccountingSummary: ObserveAccountingSummaryUseCase,
    private val exportBackup: ExportBackupUseCase
) : ViewModel() {
    private val selectedPeriod = MutableStateFlow(StatsPeriod.MONTH)
    private val searchQuery = MutableStateFlow("")
    private val searchType = MutableStateFlow<TransactionType?>(null)
    private val selectedAccountId = MutableStateFlow<Long?>(null)
    private val calendarMonthStartMillis = MutableStateFlow(startOfMonthMillis(System.currentTimeMillis()))
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

    private val summaryFlow = selectedPeriod.flatMapLatest { period ->
        observeAccountingSummary(period)
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

    private val currentMonthExpenseByCategoryFlow =
        repository.observeExpenseByCategory(
            startAt = startOfMonthMillis(System.currentTimeMillis()),
            endAt = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1)
        )

    private val periodTransactionsFlow = selectedPeriod.flatMapLatest { period ->
        val range = period.rangeContaining()
        repository.searchTransactions(
            query = "",
            startAt = range.startMillis,
            endAt = range.endMillis,
            limit = 1_000
        )
    }

    private val trendMonthStart = startOfMonthMillis(System.currentTimeMillis())
    private val trendTransactionsFlow = repository.searchTransactions(
        query = "",
        startAt = addMonthsMillis(trendMonthStart, -5),
        endAt = addMonthsMillis(trendMonthStart, 1),
        limit = 5_000
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(30_000),
        initialValue = emptyList()
    )

    private val transactionBuckets = combine(
        periodTransactionsFlow,
        trendTransactionsFlow
    ) { periodTransactions, trendTransactions ->
        TransactionBuckets(
            periodTransactions = periodTransactions,
            trendTransactions = trendTransactions
        )
    }

    private val dataState = combine(
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

    private val screenDataState = combine(
        dataState,
        planningState,
        calendarMonthTransactionsFlow,
        currentMonthExpenseByCategoryFlow,
        transactionBuckets
    ) { data, planning, calendarMonthTransactions, monthlyExpenseByCategory, buckets ->
        data.copy(
            monthlyBudget = planning.monthlyBudget,
            recurringRules = planning.recurringRules,
            categoryBudgets = planning.categoryBudgets,
            calendarMonthTransactions = calendarMonthTransactions,
            monthlyExpenseByCategory = monthlyExpenseByCategory,
            periodTransactions = buckets.periodTransactions,
            trendTransactions = buckets.trendTransactions
        )
    }

    private val filterState = combine(
        selectedPeriod,
        searchQuery,
        searchType,
        selectedAccountId
    ) { period, query, type, accountId ->
        FilterState(period, query, type, accountId)
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
            searchResults = searchResults,
            trash = trash,
            summary = summary,
            monthlyBudget = data.monthlyBudget,
            categoryBudgets = data.categoryBudgets,
            monthlyExpenseByCategory = data.monthlyExpenseByCategory,
            recurringRules = data.recurringRules,
            calendarMonthTransactions = data.calendarMonthTransactions,
            periodTransactions = data.periodTransactions,
            trendTransactions = data.trendTransactions,
            selectedPeriod = filters.period,
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
                .onFailure { showMessage(it.message ?: "初始化默认数据失败") }
        }
    }

    fun setPeriod(period: StatsPeriod) {
        selectedPeriod.value = period
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

    fun addBalanceAdjustment(
        amount: String,
        accountId: Long?,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) = transactionActions.addBalanceAdjustment(amount, accountId, note, tagIds, occurredAt)

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

    fun addAccount(name: String, type: AccountType, initialBalance: String) =
        managementActions.addAccount(name, type, initialBalance)

    fun archiveAccount(accountId: Long) = managementActions.archiveAccount(accountId)

    fun deleteTransaction(transactionId: Long) = transactionActions.deleteTransaction(transactionId)

    fun restoreTransaction(transactionId: Long) = transactionActions.restoreTransaction(transactionId)

    fun permanentlyDeleteTransaction(transactionId: Long) = transactionActions.permanentlyDeleteTransaction(transactionId)

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

    fun addCategory(name: String, kind: CategoryKind) = managementActions.addCategory(name, kind)

    fun renameCategory(id: Long, name: String) = managementActions.renameCategory(id, name)

    fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) =
        managementActions.updateCategory(id, name, iconName, colorArgb)

    fun moveCategory(id: Long, direction: Int) = managementActions.moveCategory(id, direction)

    fun deleteCategory(id: Long) = managementActions.deleteCategory(id)

    fun renameTag(id: Long, name: String) = managementActions.renameTag(id, name)

    fun deleteTag(id: Long) = managementActions.deleteTag(id)

    fun export(format: ExportFormat) = backupActions.export(format)

    fun importJson(content: String) = backupActions.importJson(content)

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
        val monthlyBudget: BudgetEntity? = null,
        val categoryBudgets: List<BudgetEntity> = emptyList(),
        val recurringRules: List<RecurringRuleEntity> = emptyList(),
        val monthlyExpenseByCategory: List<CategorySummaryRow> = emptyList(),
        val calendarMonthTransactions: List<TransactionWithDetails> = emptyList(),
        val periodTransactions: List<TransactionWithDetails> = emptyList(),
        val trendTransactions: List<TransactionWithDetails> = emptyList()
    )

    private data class TransactionBuckets(
        val periodTransactions: List<TransactionWithDetails>,
        val trendTransactions: List<TransactionWithDetails>
    )

    private data class PlanningState(
        val monthlyBudget: BudgetEntity?,
        val recurringRules: List<RecurringRuleEntity>,
        val categoryBudgets: List<BudgetEntity>
    )

    private data class FilterState(
        val period: StatsPeriod,
        val query: String,
        val type: TransactionType?,
        val accountId: Long?
    )
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
