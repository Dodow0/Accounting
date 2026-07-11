package com.dodo.accounting.ui.viewmodel

import com.dodo.accounting.ui.viewmodel.actions.BackupActions
import com.dodo.accounting.ui.viewmodel.actions.BackupUiLocalState
import com.dodo.accounting.ui.viewmodel.actions.ManagementActions
import com.dodo.accounting.ui.viewmodel.actions.PlanningActions
import com.dodo.accounting.ui.viewmodel.actions.TransactionActions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dodo.accounting.core.time.addMonthsMillis
import com.dodo.accounting.core.time.startOfMonthMillis
import com.dodo.accounting.data.local.entity.AccountEntity
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
import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.domain.repository.AccountRepository
import com.dodo.accounting.domain.repository.CatalogRepository
import com.dodo.accounting.domain.repository.PlanningRepository
import com.dodo.accounting.domain.repository.TransactionRepository
import com.dodo.accounting.domain.repository.BackupRepository
import com.dodo.accounting.domain.usecase.AddTransactionUseCase
import com.dodo.accounting.domain.usecase.ExportBackupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Settings / mine tab: management, planning, backup, trash.
 * Exposes a wide state so existing Settings screens can map with minimal renames.
 */
data class SettingsUiState(
    val accounts: List<AccountBalanceRow> = emptyList(),
    val activeAccounts: List<AccountEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val tags: List<TagEntity> = emptyList(),
    val recentTransactions: List<TransactionWithDetails> = emptyList(),
    val activeTransactionCount: Int = 0,
    val trash: List<TransactionWithDetails> = emptyList(),
    val summary: AccountingSummary? = null,
    val monthlyBudget: BudgetEntity? = null,
    val categoryBudgets: List<BudgetEntity> = emptyList(),
    val monthlyExpenseByCategory: List<CategorySummaryRow> = emptyList(),
    val recurringRules: List<RecurringRuleEntity> = emptyList(),
    val exportPreview: String = "",
    val exportContent: String = "",
    val exportFormat: ExportFormat = ExportFormat.JSON,
    val pendingImportPreview: BackupPreview? = null,
    val pendingImportContent: String = "",
    val isLoading: Boolean = true
) {
    val totalAssetsCents: Long = accounts
        .filterNot { it.account.isArchived }
        .sumOf { it.balanceCents }

    val expenseCategories: List<CategoryEntity> = categories.filter { it.kind == CategoryKind.EXPENSE }
    val incomeCategories: List<CategoryEntity> = categories.filter { it.kind == CategoryKind.INCOME }
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accounts: AccountRepository,
    private val catalog: CatalogRepository,
    private val transactions: TransactionRepository,
    private val planning: PlanningRepository,
    private val backupRepository: BackupRepository,
    private val addTransaction: AddTransactionUseCase,
    private val exportBackup: ExportBackupUseCase,
    private val messenger: UiMessenger
) : ViewModel() {
    private val backupLocalState = MutableStateFlow(BackupUiLocalState())
    private val editingTransaction = MutableStateFlow<TransactionWithDetails?>(null)

    private val transactionActions by lazy {
        TransactionActions(viewModelScope, transactions, addTransaction, editingTransaction, messenger::show)
    }
    private val planningActions by lazy {
        PlanningActions(viewModelScope, planning, messenger::show)
    }
    private val managementActions by lazy {
        ManagementActions(viewModelScope, accounts, catalog, messenger::show)
    }
    private val backupActions by lazy {
        BackupActions(viewModelScope, backupRepository, exportBackup, backupLocalState, messenger::show)
    }

    private val baseData = combine(
        accounts.observeAccountBalances(),
        accounts.observeActiveAccounts(),
        catalog.observeCategories(),
        catalog.observeTags(),
        transactions.observeRecentTransactions()
    ) { accounts, active, categories, tags, recent ->
        SettingsBase(accounts, active, categories, tags, recent)
    }

    private val countsAndTrash = combine(
        transactions.observeActiveTransactionCount(),
        transactions.observeTrash()
    ) { count, trash -> count to trash }

    private val planningState = combine(
        planning.observeMonthlyBudget(),
        planning.observeRecurringRules(),
        planning.observeActiveBudgets()
    ) { monthly, rules, activeBudgets ->
        Triple(monthly, rules, activeBudgets.filter { it.categoryId != null })
    }

    private val monthStart = startOfMonthMillis(System.currentTimeMillis())
    private val monthEnd = addMonthsMillis(monthStart, 1)
    private val monthSummaryFlow = combine(
        transactions.observePeriodSummary(monthStart, monthEnd),
        transactions.observeExpenseByCategory(monthStart, monthEnd)
    ) { totals, expenseByCategory ->
        AccountingSummary(
            periodLabel = "",
            period = StatsPeriod.MONTH,
            totals = totals,
            expenseByCategory = expenseByCategory
        )
    }

    val uiState: StateFlow<SettingsUiState> = combine(
        baseData,
        countsAndTrash,
        planningState,
        backupLocalState,
        monthSummaryFlow
    ) { base, countsTrash, plan, backup, summary ->
        SettingsUiState(
            accounts = base.accounts,
            activeAccounts = base.activeAccounts,
            categories = base.categories,
            tags = base.tags,
            recentTransactions = base.recent,
            activeTransactionCount = countsTrash.first,
            trash = countsTrash.second,
            summary = summary,
            monthlyBudget = plan.first,
            categoryBudgets = plan.third,
            monthlyExpenseByCategory = summary.expenseByCategory,
            recurringRules = plan.second,
            exportPreview = backup.exportPreview,
            exportContent = backup.exportContent,
            exportFormat = backup.exportFormat,
            pendingImportPreview = backup.pendingImportPreview,
            pendingImportContent = backup.pendingImportContent,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun addAccount(name: String, initialBalance: String, iconName: String) =
        managementActions.addAccount(name, initialBalance, iconName)

    fun updateAccount(id: Long, name: String, initialBalance: String, iconName: String, colorArgb: Long) =
        managementActions.updateAccount(id, name, initialBalance, iconName, colorArgb)

    fun archiveAccount(accountId: Long) = managementActions.archiveAccount(accountId)
    fun restoreAccount(accountId: Long) = managementActions.restoreAccount(accountId)
    fun deleteAccount(accountId: Long) = managementActions.deleteAccount(accountId)
    fun reorderAccounts(ids: List<Long>) = managementActions.reorderAccounts(ids)

    fun addTag(name: String) = managementActions.addTag(name)
    fun addCategory(name: String, kind: CategoryKind, iconName: String = if (kind == CategoryKind.EXPENSE) "receipt_long" else "work", colorArgb: Long = if (kind == CategoryKind.EXPENSE) 0xFFEA580C else 0xFF16A34A) =
        managementActions.addCategory(name, kind, iconName, colorArgb)
    fun renameCategory(id: Long, name: String) = managementActions.renameCategory(id, name)
    fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) =
        managementActions.updateCategory(id, name, iconName, colorArgb)
    fun moveCategory(id: Long, direction: Int) = managementActions.moveCategory(id, direction)
    fun reorderCategories(ids: List<Long>) = managementActions.reorderCategories(ids)
    fun deleteCategory(id: Long) = managementActions.deleteCategory(id)
    fun renameTag(id: Long, name: String) = managementActions.renameTag(id, name)
    fun reorderTags(ids: List<Long>) = managementActions.reorderTags(ids)
    fun deleteTag(id: Long) = managementActions.deleteTag(id)

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
        name, type, amount, occurredAt, accountId, fromAccountId, toAccountId, categoryId, merchant, note
    )
    fun runDueRecurringRules() = planningActions.runDueRecurringRules()
    fun setRecurringRuleEnabled(id: Long, enabled: Boolean) = planningActions.setRecurringRuleEnabled(id, enabled)
    fun deleteRecurringRule(id: Long) = planningActions.deleteRecurringRule(id)

    fun restoreTransaction(transactionId: Long) = transactionActions.restoreTransaction(transactionId)
    fun permanentlyDeleteTransaction(transactionId: Long) = transactionActions.permanentlyDeleteTransaction(transactionId)
    fun clearTrash() = transactionActions.clearTrash()
    fun moveAllTransactionsToTrash() = transactionActions.moveAllTransactionsToTrash()

    fun export(format: ExportFormat) = backupActions.export(format)
    suspend fun exportJsonContent(): Result<String> = runCatching { exportBackup.json() }
    fun importJson(content: String) = backupActions.previewImportJson(content)
    fun confirmImportJson() = backupActions.confirmImportJson()
    fun cancelImportJson() = backupActions.cancelImportJson()
}

private data class SettingsBase(
    val accounts: List<AccountBalanceRow>,
    val activeAccounts: List<AccountEntity>,
    val categories: List<CategoryEntity>,
    val tags: List<TagEntity>,
    val recent: List<TransactionWithDetails>
)
