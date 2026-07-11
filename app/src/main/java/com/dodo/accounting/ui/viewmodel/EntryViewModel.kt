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
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.domain.repository.AccountRepository
import com.dodo.accounting.domain.repository.CatalogRepository
import com.dodo.accounting.domain.repository.PlanningRepository
import com.dodo.accounting.domain.repository.TransactionRepository
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

/**
 * Activity-scoped entry sheet state: accounts/categories/tags + edit target + budget hints.
 * Form field state remains in the composable for this refactor.
 */
data class EntryUiState(
    val accounts: List<AccountBalanceRow> = emptyList(),
    val activeAccounts: List<AccountEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val tags: List<TagEntity> = emptyList(),
    val recentTransactions: List<TransactionWithDetails> = emptyList(),
    val entryMonthExpenseByCategory: List<CategorySummaryRow> = emptyList(),
    val monthlyBudget: com.dodo.accounting.data.local.entity.BudgetEntity? = null,
    val categoryBudgets: List<com.dodo.accounting.data.local.entity.BudgetEntity> = emptyList(),
    val editingTransaction: TransactionWithDetails? = null,
    val isLoading: Boolean = true
) {
    val expenseCategories: List<CategoryEntity> = categories.filter { it.kind == CategoryKind.EXPENSE }
    val incomeCategories: List<CategoryEntity> = categories.filter { it.kind == CategoryKind.INCOME }
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EntryViewModel @Inject constructor(
    private val accounts: AccountRepository,
    private val catalog: CatalogRepository,
    private val transactions: TransactionRepository,
    private val planning: PlanningRepository,
    private val addTransaction: AddTransactionUseCase,
    private val messenger: UiMessenger
) : ViewModel() {
    private val entryMonthStartMillis = MutableStateFlow(startOfMonthMillis(System.currentTimeMillis()))
    private val editingTransaction = MutableStateFlow<TransactionWithDetails?>(null)

    private val transactionActions by lazy {
        TransactionActions(viewModelScope, transactions, addTransaction, editingTransaction, messenger::show)
    }
    private val managementActions by lazy {
        ManagementActions(viewModelScope, accounts, catalog, messenger::show)
    }

    private val baseFlow = combine(
        accounts.observeAccountBalances(),
        accounts.observeActiveAccounts(),
        catalog.observeCategories(),
        catalog.observeTags(),
        transactions.observeRecentTransactions()
    ) { accounts, activeAccounts, categories, tags, recent ->
        EntryBase(accounts, activeAccounts, categories, tags, recent)
    }

    private val planningFlow = combine(
        planning.observeMonthlyBudget(),
        planning.observeActiveBudgets()
    ) { monthly, active ->
        monthly to active.filter { it.categoryId != null }
    }

    private val entryMonthExpenseFlow = entryMonthStartMillis.flatMapLatest { monthStart ->
        transactions.observeExpenseByCategory(
            startAt = monthStart,
            endAt = addMonthsMillis(monthStart, 1)
        )
    }

    val uiState: StateFlow<EntryUiState> = combine(
        baseFlow,
        planningFlow,
        entryMonthExpenseFlow,
        editingTransaction
    ) { base, planning, entryExpense, editing ->
        EntryUiState(
            accounts = base.accounts,
            activeAccounts = base.activeAccounts,
            categories = base.categories,
            tags = base.tags,
            recentTransactions = base.recent,
            entryMonthExpenseByCategory = entryExpense,
            monthlyBudget = planning.first,
            categoryBudgets = planning.second,
            editingTransaction = editing,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EntryUiState()
    )

    fun setEntryOccurredAt(millis: Long) {
        entryMonthStartMillis.value = startOfMonthMillis(millis)
    }

    fun startEditTransaction(transaction: TransactionWithDetails) =
        transactionActions.startEditTransaction(transaction)

    fun cancelEditTransaction() = transactionActions.cancelEditTransaction()

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
        type, amount, accountId, fromAccountId, toAccountId, categoryId, merchant, note, tagIds, occurredAt
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
        transactionId, type, amount, accountId, fromAccountId, toAccountId, categoryId, merchant, note, tagIds, occurredAt
    )

    fun deleteTransaction(transactionId: Long) = transactionActions.deleteTransaction(transactionId)

    fun addTag(name: String) = managementActions.addTag(name)
}

private data class EntryBase(
    val accounts: List<AccountBalanceRow>,
    val activeAccounts: List<AccountEntity>,
    val categories: List<CategoryEntity>,
    val tags: List<TagEntity>,
    val recent: List<TransactionWithDetails>
)
