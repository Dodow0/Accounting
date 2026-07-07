package com.dodo.accounting.domain.repository

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
import com.dodo.accounting.data.local.model.PeriodSummaryRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import com.dodo.accounting.domain.model.AccountRemovalResult
import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.domain.model.RecurringGenerationResult
import com.dodo.accounting.domain.model.TransactionDraft
import kotlinx.coroutines.flow.Flow

interface AccountingRepository {
    fun observeAccountBalances(): Flow<List<AccountBalanceRow>>
    fun observeAccounts(): Flow<List<AccountEntity>>
    fun observeActiveAccounts(): Flow<List<AccountEntity>>
    fun observeCategories(): Flow<List<CategoryEntity>>
    fun observeCategories(kind: CategoryKind): Flow<List<CategoryEntity>>
    fun observeTags(): Flow<List<TagEntity>>
    fun observeRecentTransactions(limit: Int = 50): Flow<List<TransactionWithDetails>>
    fun observeActiveTransactionCount(): Flow<Int>
    fun searchTransactions(
        query: String,
        type: TransactionType? = null,
        accountId: Long? = null,
        startAt: Long? = null,
        endAt: Long? = null,
        limit: Int = 200,
        offset: Int = 0
    ): Flow<List<TransactionWithDetails>>
    fun observeTrash(limit: Int = 100): Flow<List<TransactionWithDetails>>
    fun observePeriodSummary(startAt: Long, endAt: Long): Flow<PeriodSummaryRow>
    fun observeExpenseByCategory(startAt: Long, endAt: Long): Flow<List<CategorySummaryRow>>
    fun observeMonthlyTrend(startAt: Long, endAt: Long): Flow<List<TrendSummaryRow>>
    fun observeActiveBudgets(): Flow<List<BudgetEntity>>
    fun observeMonthlyBudget(): Flow<BudgetEntity?>
    fun observeRecurringRules(): Flow<List<RecurringRuleEntity>>

    suspend fun addAccount(account: AccountEntity): Long
    suspend fun updateAccount(
        id: Long,
        name: String,
        type: AccountType,
        initialBalanceCents: Long,
        iconName: String,
        colorArgb: Long
    )
    suspend fun archiveAccount(id: Long, archived: Boolean): AccountRemovalResult
    suspend fun deleteAccount(id: Long): AccountRemovalResult
    suspend fun addCategory(category: CategoryEntity): Long
    suspend fun renameCategory(id: Long, name: String)
    suspend fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long)
    suspend fun moveCategory(id: Long, direction: Int)
    suspend fun deleteCategory(id: Long)
    suspend fun addTransaction(draft: TransactionDraft): Long
    suspend fun updateTransaction(id: Long, draft: TransactionDraft)
    suspend fun softDeleteTransaction(id: Long)
    suspend fun softDeleteAllTransactions(): Int
    suspend fun restoreTransaction(id: Long)
    suspend fun permanentlyDeleteTransaction(id: Long)
    suspend fun clearTrash()
    suspend fun setMonthlyBudget(amountCents: Long): Long
    suspend fun setCategoryBudget(categoryId: Long, categoryName: String, amountCents: Long): Long
    suspend fun addRecurringRule(rule: RecurringRuleEntity): Long
    suspend fun setRecurringRuleEnabled(id: Long, enabled: Boolean)
    suspend fun deleteRecurringRule(id: Long)
    suspend fun generateDueRecurringTransactions(): RecurringGenerationResult
    suspend fun addTag(name: String): Long
    suspend fun renameTag(id: Long, name: String)
    suspend fun deleteTag(id: Long)
    suspend fun ensureSeedData()
    suspend fun exportJson(): String
    suspend fun exportCsv(): String
    suspend fun previewImportJson(content: String): BackupPreview
    suspend fun importJson(content: String)
}
