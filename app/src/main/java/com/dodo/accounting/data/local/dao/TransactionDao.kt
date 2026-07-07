package com.dodo.accounting.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionTagCrossRef
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.data.local.model.PeriodSummaryRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.data.local.model.TrendSummaryRow
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Transaction
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionWithDetails(id: Long): TransactionWithDetails?

    @Transaction
    @Query(
        """
        SELECT * FROM transactions
        WHERE deletedAt IS NULL
        ORDER BY occurredAt DESC, createdAt DESC
        LIMIT :limit
        """
    )
    fun observeRecent(limit: Int = 50): Flow<List<TransactionWithDetails>>

    @Query("SELECT COUNT(*) FROM transactions WHERE deletedAt IS NULL")
    fun observeActiveCount(): Flow<Int>

    @Transaction
    @Query(
        """
        SELECT DISTINCT transactions.* FROM transactions
        LEFT JOIN categories ON categories.id = transactions.categoryId AND categories.deletedAt IS NULL
        LEFT JOIN accounts directAccount ON directAccount.id = transactions.accountId
        LEFT JOIN accounts fromAccount ON fromAccount.id = transactions.fromAccountId
        LEFT JOIN accounts toAccount ON toAccount.id = transactions.toAccountId
        LEFT JOIN transaction_tags ON transaction_tags.transactionId = transactions.id
        LEFT JOIN tags ON tags.id = transaction_tags.tagId
        WHERE transactions.deletedAt IS NULL
            AND (:query = '' OR transactions.note LIKE '%' || :query || '%'
                OR transactions.merchant LIKE '%' || :query || '%'
                OR categories.name LIKE '%' || :query || '%'
                OR directAccount.name LIKE '%' || :query || '%'
                OR fromAccount.name LIKE '%' || :query || '%'
                OR toAccount.name LIKE '%' || :query || '%'
                OR tags.name LIKE '%' || :query || '%')
            AND (:type IS NULL OR transactions.type = :type)
            AND (:accountId IS NULL OR transactions.accountId = :accountId OR transactions.fromAccountId = :accountId OR transactions.toAccountId = :accountId)
            AND (:startAt IS NULL OR transactions.occurredAt >= :startAt)
            AND (:endAt IS NULL OR transactions.occurredAt < :endAt)
        ORDER BY transactions.occurredAt DESC, transactions.createdAt DESC
        LIMIT :limit OFFSET :offset
        """
    )
    fun search(
        query: String,
        type: TransactionType?,
        accountId: Long?,
        startAt: Long?,
        endAt: Long?,
        limit: Int = 200,
        offset: Int = 0
    ): Flow<List<TransactionWithDetails>>

    @Transaction
    @Query(
        """
        SELECT * FROM transactions
        WHERE deletedAt IS NOT NULL
        ORDER BY deletedAt DESC
        LIMIT :limit
        """
    )
    fun observeTrash(limit: Int = 100): Flow<List<TransactionWithDetails>>

    @Query(
        """
        SELECT
            COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amountCents ELSE 0 END), 0) AS expenseCents,
            COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amountCents ELSE 0 END), 0) AS incomeCents,
            COALESCE(SUM(CASE WHEN type = 'TRANSFER' THEN amountCents ELSE 0 END), 0) AS transferCents,
            COALESCE(SUM(CASE WHEN type = 'BALANCE_ADJUSTMENT' THEN amountCents ELSE 0 END), 0) AS adjustmentCents
        FROM transactions
        WHERE deletedAt IS NULL AND occurredAt >= :startAt AND occurredAt < :endAt
        """
    )
    fun observePeriodSummary(startAt: Long, endAt: Long): Flow<PeriodSummaryRow>

    @Query(
        """
        SELECT categories.id AS categoryId, categories.name AS categoryName, SUM(transactions.amountCents) AS amountCents
        FROM transactions
        LEFT JOIN categories ON categories.id = transactions.categoryId AND categories.deletedAt IS NULL
        WHERE transactions.deletedAt IS NULL
            AND transactions.type = 'EXPENSE'
            AND transactions.occurredAt >= :startAt
            AND transactions.occurredAt < :endAt
        GROUP BY categories.id
        ORDER BY amountCents DESC
        """
    )
    fun observeExpenseByCategory(startAt: Long, endAt: Long): Flow<List<CategorySummaryRow>>

    @Query(
        """
        SELECT
            strftime('%Y-%m', transactions.occurredAt / 1000, 'unixepoch', 'localtime') AS bucketMonth,
            COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amountCents ELSE 0 END), 0) AS expenseCents,
            COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amountCents ELSE 0 END), 0) AS incomeCents,
            COUNT(*) AS count
        FROM transactions
        WHERE deletedAt IS NULL
            AND type IN ('EXPENSE', 'INCOME')
            AND occurredAt >= :startAt
            AND occurredAt < :endAt
        GROUP BY bucketMonth
        ORDER BY bucketMonth ASC
        """
    )
    fun observeMonthlyTrend(startAt: Long, endAt: Long): Flow<List<TrendSummaryRow>>

    @Query(
        """
        SELECT * FROM transactions
        WHERE deletedAt IS NULL
        ORDER BY occurredAt ASC, createdAt ASC
        """
    )
    suspend fun getAllActive(): List<TransactionEntity>

    @Query(
        """
        SELECT * FROM transactions
        ORDER BY occurredAt ASC, createdAt ASC
        """
    )
    suspend fun getTransactionsSnapshot(): List<TransactionEntity>

    @Query(
        """
        SELECT COUNT(*) FROM transactions
        WHERE accountId = :accountId OR fromAccountId = :accountId OR toAccountId = :accountId
        """
    )
    suspend fun countReferencingAccount(accountId: Long): Int

    @Query("UPDATE transactions SET categoryId = NULL, updatedAt = :updatedAt WHERE categoryId = :categoryId")
    suspend fun clearCategoryReferences(categoryId: Long, updatedAt: Long = System.currentTimeMillis()): Int

    @Query("SELECT * FROM transaction_tags ORDER BY transactionId ASC, tagId ASC")
    suspend fun getTransactionTagRefsSnapshot(): List<TransactionTagCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTagRefs(refs: List<TransactionTagCrossRef>)

    @Query("DELETE FROM transaction_tags WHERE transactionId = :transactionId")
    suspend fun clearTagRefs(transactionId: Long)

    @Query("DELETE FROM transaction_tags")
    suspend fun clearAllTagRefs()

    @Query("UPDATE transactions SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long = System.currentTimeMillis())

    @Query("UPDATE transactions SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE deletedAt IS NULL")
    suspend fun softDeleteAllActive(deletedAt: Long = System.currentTimeMillis()): Int

    @Query("UPDATE transactions SET deletedAt = NULL, updatedAt = :updatedAt WHERE id = :id")
    suspend fun restore(id: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun permanentlyDelete(id: Long)

    @Query("DELETE FROM transactions WHERE deletedAt IS NOT NULL")
    suspend fun permanentlyDeleteTrash()

    @Query("DELETE FROM transactions")
    suspend fun clearAll()
}
