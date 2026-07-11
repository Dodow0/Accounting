package com.dodo.accounting.data.repository.parts

import androidx.room.withTransaction
import com.dodo.accounting.data.local.AccountingDatabase
import com.dodo.accounting.data.local.SeedData
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.BudgetEntity
import com.dodo.accounting.data.local.entity.BudgetPeriod
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionSource
import com.dodo.accounting.data.local.entity.TransactionTagCrossRef
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.data.local.model.PeriodSummaryRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import com.dodo.accounting.domain.model.AccountRemovalAction
import com.dodo.accounting.domain.model.AccountRemovalResult
import com.dodo.accounting.domain.model.RecurringGenerationResult
import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.model.TransactionRules
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.flow.Flow

internal class TransactionRepositoryOps(
    private val database: AccountingDatabase
) {
    private val transactionDao = database.transactionDao()

    fun observeRecentTransactions(limit: Int): Flow<List<TransactionWithDetails>> = transactionDao.observeRecent(limit)
    fun observeActiveTransactionCount(): Flow<Int> = transactionDao.observeActiveCount()
    fun searchTransactions(
        query: String,
        type: TransactionType?,
        accountId: Long?,
        startAt: Long?,
        endAt: Long?,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionWithDetails>> = transactionDao.search(query, type, accountId, startAt, endAt, limit, offset)
    fun observeTrash(limit: Int): Flow<List<TransactionWithDetails>> = transactionDao.observeTrash(limit)
    fun observePeriodSummary(startAt: Long, endAt: Long): Flow<PeriodSummaryRow> =
        transactionDao.observePeriodSummary(startAt, endAt)
    fun observeExpenseByCategory(startAt: Long, endAt: Long): Flow<List<CategorySummaryRow>> =
        transactionDao.observeExpenseByCategory(startAt, endAt)
    fun observeMonthlyTrend(startAt: Long, endAt: Long): Flow<List<TrendSummaryRow>> =
        transactionDao.observeMonthlyTrend(startAt, endAt)

    suspend fun addTransaction(draft: TransactionDraft): Long = database.withTransaction {
        insertTransaction(draft)
    }

    suspend fun updateTransaction(id: Long, draft: TransactionDraft) = database.withTransaction {
        TransactionRules.validate(draft)
        val existing = transactionDao.getTransactionWithDetails(id)?.transaction
            ?: error("流水不存在")
        val now = System.currentTimeMillis()
        transactionDao.update(
            existing.copy(
                type = draft.type,
                amountCents = draft.amountCents,
                occurredAt = draft.occurredAt,
                accountId = draft.accountId,
                fromAccountId = draft.fromAccountId,
                toAccountId = draft.toAccountId,
                categoryId = draft.categoryId,
                merchant = draft.merchant.trim(),
                note = draft.note.trim(),
                updatedAt = now
            )
        )
        replaceTagRefs(id, draft.tagIds)
    }

    suspend fun softDeleteTransaction(id: Long) {
        transactionDao.softDelete(id)
    }

    suspend fun softDeleteAllTransactions(): Int = database.withTransaction {
        transactionDao.softDeleteAllActive()
    }

    suspend fun restoreTransaction(id: Long) {
        transactionDao.restore(id)
    }

    suspend fun permanentlyDeleteTransaction(id: Long) {
        transactionDao.permanentlyDelete(id)
    }

    suspend fun clearTrash() = database.withTransaction {
        transactionDao.permanentlyDeleteTrash()
    }

    internal suspend fun insertTransaction(draft: TransactionDraft): Long {
        TransactionRules.validate(draft)
        val transactionId = transactionDao.insert(
            TransactionEntity(
                type = draft.type,
                amountCents = draft.amountCents,
                occurredAt = draft.occurredAt,
                accountId = draft.accountId,
                fromAccountId = draft.fromAccountId,
                toAccountId = draft.toAccountId,
                categoryId = draft.categoryId,
                merchant = draft.merchant.trim(),
                note = draft.note.trim(),
                source = draft.source
            )
        )
        insertTagRefs(transactionId, draft.tagIds)
        return transactionId
    }

    private suspend fun replaceTagRefs(transactionId: Long, tagIds: List<Long>) {
        transactionDao.clearTagRefs(transactionId)
        insertTagRefs(transactionId, tagIds)
    }

    private suspend fun insertTagRefs(transactionId: Long, tagIds: List<Long>) {
        if (tagIds.isNotEmpty()) {
            transactionDao.insertTagRefs(
                tagIds.distinct().map { tagId ->
                    TransactionTagCrossRef(transactionId = transactionId, tagId = tagId)
                }
            )
        }
    }
}
