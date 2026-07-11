package com.dodo.accounting.data.repository

import com.dodo.accounting.data.local.AccountingDatabase
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.repository.parts.AccountRepositoryOps
import com.dodo.accounting.data.repository.parts.CatalogRepositoryOps
import com.dodo.accounting.data.repository.parts.PlanningRepositoryOps
import com.dodo.accounting.data.repository.parts.TransactionRepositoryOps
import com.dodo.accounting.domain.model.AccountRemovalResult
import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.domain.model.RecurringGenerationResult
import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.repository.AccountingRepository
import com.dodo.accounting.domain.repository.BackupRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Facade over aggregate repository operations. Prefer injecting [BackupRepository]
 * (or future aggregate interfaces) for new code; this type preserves existing call sites.
 */
@Singleton
class AccountingRepositoryImpl @Inject constructor(
    database: AccountingDatabase,
    private val backupRepository: BackupRepository
) : AccountingRepository {
    private val accounts = AccountRepositoryOps(database)
    private val catalog = CatalogRepositoryOps(database)
    private val transactions = TransactionRepositoryOps(database)
    private val planning = PlanningRepositoryOps(database, transactions)

    override fun observeAccountBalances() = accounts.observeAccountBalances()
    override fun observeAccounts() = accounts.observeAccounts()
    override fun observeActiveAccounts() = accounts.observeActiveAccounts()
    override fun observeCategories() = catalog.observeCategories()
    override fun observeCategories(kind: CategoryKind) = catalog.observeCategories(kind)
    override fun observeTags() = catalog.observeTags()
    override fun observeRecentTransactions(limit: Int) = transactions.observeRecentTransactions(limit)
    override fun observeActiveTransactionCount() = transactions.observeActiveTransactionCount()
    override fun searchTransactions(
        query: String,
        type: TransactionType?,
        accountId: Long?,
        startAt: Long?,
        endAt: Long?,
        limit: Int,
        offset: Int
    ) = transactions.searchTransactions(query, type, accountId, startAt, endAt, limit, offset)
    override fun observeTrash(limit: Int) = transactions.observeTrash(limit)
    override fun observePeriodSummary(startAt: Long, endAt: Long) = transactions.observePeriodSummary(startAt, endAt)
    override fun observeExpenseByCategory(startAt: Long, endAt: Long) = transactions.observeExpenseByCategory(startAt, endAt)
    override fun observeMonthlyTrend(startAt: Long, endAt: Long) = transactions.observeMonthlyTrend(startAt, endAt)
    override fun observeActiveBudgets() = planning.observeActiveBudgets()
    override fun observeMonthlyBudget() = planning.observeMonthlyBudget()
    override fun observeRecurringRules() = planning.observeRecurringRules()

    override suspend fun addAccount(account: AccountEntity) = accounts.addAccount(account)
    override suspend fun updateAccount(
        id: Long,
        name: String,
        initialBalanceCents: Long,
        iconName: String,
        colorArgb: Long
    ) = accounts.updateAccount(id, name, initialBalanceCents, iconName, colorArgb)
    override suspend fun archiveAccount(id: Long, archived: Boolean) = accounts.archiveAccount(id, archived)
    override suspend fun deleteAccount(id: Long) = accounts.deleteAccount(id)
    override suspend fun reorderAccounts(ids: List<Long>) = accounts.reorderAccounts(ids)

    override suspend fun addCategory(category: CategoryEntity) = catalog.addCategory(category)
    override suspend fun renameCategory(id: Long, name: String) = catalog.renameCategory(id, name)
    override suspend fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) =
        catalog.updateCategory(id, name, iconName, colorArgb)
    override suspend fun moveCategory(id: Long, direction: Int) = catalog.moveCategory(id, direction)
    override suspend fun reorderCategories(ids: List<Long>) = catalog.reorderCategories(ids)
    override suspend fun deleteCategory(id: Long) = catalog.deleteCategory(id)

    override suspend fun addTransaction(draft: TransactionDraft) = transactions.addTransaction(draft)
    override suspend fun updateTransaction(id: Long, draft: TransactionDraft) = transactions.updateTransaction(id, draft)
    override suspend fun softDeleteTransaction(id: Long) = transactions.softDeleteTransaction(id)
    override suspend fun softDeleteAllTransactions() = transactions.softDeleteAllTransactions()
    override suspend fun restoreTransaction(id: Long) = transactions.restoreTransaction(id)
    override suspend fun permanentlyDeleteTransaction(id: Long) = transactions.permanentlyDeleteTransaction(id)
    override suspend fun clearTrash() = transactions.clearTrash()

    override suspend fun setMonthlyBudget(amountCents: Long) = planning.setMonthlyBudget(amountCents)
    override suspend fun setCategoryBudget(categoryId: Long, categoryName: String, amountCents: Long) =
        planning.setCategoryBudget(categoryId, categoryName, amountCents)
    override suspend fun addRecurringRule(rule: RecurringRuleEntity) = planning.addRecurringRule(rule)
    override suspend fun setRecurringRuleEnabled(id: Long, enabled: Boolean) = planning.setRecurringRuleEnabled(id, enabled)
    override suspend fun deleteRecurringRule(id: Long) = planning.deleteRecurringRule(id)
    override suspend fun generateDueRecurringTransactions() = planning.generateDueRecurringTransactions()

    override suspend fun addTag(name: String) = catalog.addTag(name)
    override suspend fun renameTag(id: Long, name: String) = catalog.renameTag(id, name)
    override suspend fun reorderTags(ids: List<Long>) = catalog.reorderTags(ids)
    override suspend fun deleteTag(id: Long) = catalog.deleteTag(id)

    override suspend fun ensureSeedData() = planning.ensureSeedData()

    override suspend fun exportJson(): String = backupRepository.exportJson()
    override suspend fun exportCsv(): String = backupRepository.exportCsv()
    override suspend fun previewImportJson(content: String): BackupPreview = backupRepository.previewImportJson(content)
    override suspend fun importJson(content: String) = backupRepository.importJson(content)
}
