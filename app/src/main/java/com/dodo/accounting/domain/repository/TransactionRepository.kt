package com.dodo.accounting.domain.repository

import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.data.local.model.PeriodSummaryRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import com.dodo.accounting.domain.model.TransactionDraft
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
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

    suspend fun addTransaction(draft: TransactionDraft): Long
    suspend fun updateTransaction(id: Long, draft: TransactionDraft)
    suspend fun softDeleteTransaction(id: Long)
    suspend fun softDeleteAllTransactions(): Int
    suspend fun restoreTransaction(id: Long)
    suspend fun permanentlyDeleteTransaction(id: Long)
    suspend fun clearTrash()
}
