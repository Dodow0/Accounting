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
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.domain.model.StatsPeriod
import com.dodo.accounting.domain.model.TransactionDraft
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

    private val trendTransactionsFlow =
        repository.searchTransactions(
            query = "",
            startAt = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), -5),
            endAt = addMonthsMillis(startOfMonthMillis(System.currentTimeMillis()), 1),
            limit = 5_000
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
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.EXPENSE,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                accountId = accountId,
                categoryId = categoryId,
                merchant = merchant,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "支出已记录"
        )
    }

    fun addIncome(
        amount: String,
        accountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.INCOME,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                accountId = accountId,
                categoryId = categoryId,
                merchant = merchant,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "收入已记录"
        )
    }

    fun addTransfer(
        amount: String,
        fromAccountId: Long?,
        toAccountId: Long?,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.TRANSFER,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "转账已记录"
        )
    }

    fun addBalanceAdjustment(
        amount: String,
        accountId: Long?,
        note: String,
        tagIds: List<Long> = emptyList(),
        occurredAt: Long = System.currentTimeMillis()
    ) {
        submitDraft(
            TransactionDraft(
                type = TransactionType.BALANCE_ADJUSTMENT,
                amountCents = Money.fromMajor(amount).cents,
                occurredAt = occurredAt,
                accountId = accountId,
                note = note,
                tagIds = tagIds
            ),
            successMessage = "余额校正已记录"
        )
    }

    fun startEditTransaction(transaction: TransactionWithDetails) {
        localState.update { it.copy(editingTransaction = transaction) }
    }

    fun cancelEditTransaction() {
        localState.update { it.copy(editingTransaction = null) }
    }

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
    ) {
        val draft = createDraft(
            type = type,
            amount = amount,
            occurredAt = occurredAt,
            accountId = accountId,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            categoryId = categoryId,
            merchant = merchant,
            note = note,
            tagIds = tagIds
        )
        viewModelScope.launch {
            runCatching { repository.updateTransaction(transactionId, draft) }
                .onSuccess {
                    localState.update { state -> state.copy(editingTransaction = null) }
                    showMessage("流水已更新")
                }
                .onFailure { showMessage(it.message ?: "更新失败") }
        }
    }

    fun addAccount(
        name: String,
        type: AccountType,
        initialBalance: String
    ) {
        viewModelScope.launch {
            runCatching {
                require(name.isNotBlank()) { "账户名称不能为空" }
                repository.addAccount(
                    AccountEntity(
                        name = name.trim(),
                        type = type,
                        initialBalanceCents = Money.fromMajor(initialBalance).cents,
                        sortOrder = System.currentTimeMillis().toInt()
                    )
                )
            }.onSuccess {
                showMessage("资产账户已添加")
            }.onFailure {
                showMessage(it.message ?: "添加账户失败")
            }
        }
    }

    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            runCatching { repository.softDeleteTransaction(transactionId) }
                .onSuccess { showMessage("已移入回收站") }
                .onFailure { showMessage(it.message ?: "删除失败") }
        }
    }

    fun restoreTransaction(transactionId: Long) {
        viewModelScope.launch {
            runCatching { repository.restoreTransaction(transactionId) }
                .onSuccess { showMessage("已恢复") }
                .onFailure { showMessage(it.message ?: "恢复失败") }
        }
    }

    fun permanentlyDeleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            runCatching { repository.permanentlyDeleteTransaction(transactionId) }
                .onSuccess { showMessage("已彻底删除") }
                .onFailure { showMessage(it.message ?: "彻底删除失败") }
        }
    }

    fun setMonthlyBudget(amount: String) {
        viewModelScope.launch {
            runCatching { repository.setMonthlyBudget(Money.fromMajor(amount).cents) }
                .onSuccess { showMessage("月度预算已更新") }
                .onFailure { showMessage(it.message ?: "预算设置失败") }
        }
    }

    fun setCategoryBudget(category: CategoryEntity, amount: String) {
        viewModelScope.launch {
            runCatching { repository.setCategoryBudget(category.id, category.name, Money.fromMajor(amount).cents) }
                .onSuccess { showMessage("${category.name} 预算已更新") }
                .onFailure { showMessage(it.message ?: "分类预算设置失败") }
        }
    }

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
    ) {
        viewModelScope.launch {
            runCatching {
                repository.addRecurringRule(
                    RecurringRuleEntity(
                        name = name.trim(),
                        transactionType = type,
                        amountCents = Money.fromMajor(amount).cents,
                        accountId = if (type == TransactionType.TRANSFER) null else accountId,
                        fromAccountId = if (type == TransactionType.TRANSFER) fromAccountId else null,
                        toAccountId = if (type == TransactionType.TRANSFER) toAccountId else null,
                        categoryId = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) categoryId else null,
                        merchant = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) merchant else "",
                        note = note,
                        intervalMonths = 1,
                        nextRunAt = occurredAt
                    )
                )
                repository.generateDueRecurringTransactions()
            }.onSuccess { generated ->
                showMessage(if (generated > 0) "周期规则已添加，已生成 $generated 条账单" else "周期规则已添加")
            }.onFailure {
                showMessage(it.message ?: "添加周期规则失败")
            }
        }
    }

    fun runDueRecurringRules() {
        viewModelScope.launch {
            runCatching { repository.generateDueRecurringTransactions() }
                .onSuccess { showMessage("已生成 $it 条到期账单") }
                .onFailure { showMessage(it.message ?: "生成周期账单失败") }
        }
    }

    fun setRecurringRuleEnabled(id: Long, enabled: Boolean) {
        viewModelScope.launch {
            runCatching { repository.setRecurringRuleEnabled(id, enabled) }
                .onSuccess { showMessage(if (enabled) "周期规则已启用" else "周期规则已停用") }
                .onFailure { showMessage(it.message ?: "更新周期规则失败") }
        }
    }

    fun deleteRecurringRule(id: Long) {
        viewModelScope.launch {
            runCatching { repository.deleteRecurringRule(id) }
                .onSuccess { showMessage("周期规则已删除") }
                .onFailure { showMessage(it.message ?: "删除周期规则失败") }
        }
    }

    fun addTag(name: String) {
        viewModelScope.launch {
            runCatching { repository.addTag(name) }
                .onSuccess { showMessage("标签已添加") }
                .onFailure { showMessage(it.message ?: "添加标签失败") }
        }
    }

    fun addCategory(name: String, kind: CategoryKind) {
        viewModelScope.launch {
            runCatching {
                repository.addCategory(
                    CategoryEntity(
                        name = name,
                        kind = kind,
                        colorArgb = if (kind == CategoryKind.EXPENSE) 0xFFEA580C else 0xFF16A34A,
                        iconName = if (kind == CategoryKind.EXPENSE) "receipt_long" else "work",
                        sortOrder = System.currentTimeMillis().toInt()
                    )
                )
            }
                .onSuccess { showMessage("分类已添加") }
                .onFailure { showMessage(it.message ?: "添加分类失败") }
        }
    }

    fun renameCategory(id: Long, name: String) {
        viewModelScope.launch {
            runCatching { repository.renameCategory(id, name) }
                .onSuccess { showMessage("分类已更新") }
                .onFailure { showMessage(it.message ?: "更新分类失败") }
        }
    }

    fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) {
        viewModelScope.launch {
            runCatching { repository.updateCategory(id, name, iconName, colorArgb) }
                .onSuccess { showMessage("分类已更新") }
                .onFailure { showMessage(it.message ?: "更新分类失败") }
        }
    }

    fun moveCategory(id: Long, direction: Int) {
        viewModelScope.launch {
            runCatching { repository.moveCategory(id, direction) }
                .onFailure { showMessage(it.message ?: "分类排序失败") }
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            runCatching { repository.deleteCategory(id) }
                .onSuccess { showMessage("分类已删除") }
                .onFailure { showMessage(it.message ?: "删除分类失败") }
        }
    }

    fun renameTag(id: Long, name: String) {
        viewModelScope.launch {
            runCatching { repository.renameTag(id, name) }
                .onSuccess { showMessage("标签已更新") }
                .onFailure { showMessage(it.message ?: "更新标签失败") }
        }
    }

    fun deleteTag(id: Long) {
        viewModelScope.launch {
            runCatching { repository.deleteTag(id) }
                .onSuccess { showMessage("标签已删除") }
                .onFailure { showMessage(it.message ?: "删除标签失败") }
        }
    }

    fun export(format: ExportFormat) {
        viewModelScope.launch {
            localState.update { it.copy(exportFormat = format) }
            runCatching {
                when (format) {
                    ExportFormat.JSON -> exportBackup.json()
                    ExportFormat.CSV -> exportBackup.csv()
                }
            }.onSuccess { content ->
                localState.update {
                    it.copy(
                        exportPreview = content.take(12_000),
                        exportContent = content,
                        exportFormat = format
                    )
                }
                showMessage("${format.name} 已生成预览")
            }.onFailure {
                showMessage(it.message ?: "导出失败")
            }
        }
    }

    fun importJson(content: String) {
        viewModelScope.launch {
            runCatching { repository.importJson(content) }
                .onSuccess {
                    localState.update { state -> state.copy(exportPreview = "", exportContent = "") }
                    showMessage("JSON 备份已导入")
                }
                .onFailure { showMessage(it.message ?: "导入失败") }
        }
    }

    fun clearMessage() {
        localState.update { it.copy(message = null) }
    }

    private fun submitDraft(draft: TransactionDraft, successMessage: String) {
        viewModelScope.launch {
            runCatching { addTransaction(draft) }
                .onSuccess { showMessage(successMessage) }
                .onFailure { showMessage(it.message ?: "记录失败") }
        }
    }

    private fun createDraft(
        type: TransactionType,
        amount: String,
        occurredAt: Long,
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String,
        tagIds: List<Long>
    ): TransactionDraft {
        return TransactionDraft(
            type = type,
            amountCents = Money.fromMajor(amount).cents,
            occurredAt = occurredAt,
            accountId = if (type == TransactionType.TRANSFER) null else accountId,
            fromAccountId = if (type == TransactionType.TRANSFER) fromAccountId else null,
            toAccountId = if (type == TransactionType.TRANSFER) toAccountId else null,
            categoryId = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) categoryId else null,
            merchant = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) merchant else "",
            note = note,
            tagIds = tagIds
        )
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
