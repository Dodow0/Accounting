package com.dodo.accounting.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.model.AccountBalanceRow
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query(
        """
        SELECT
            accounts.*,
            COALESCE(SUM(CASE WHEN transactions.type = 'INCOME' AND transactions.accountId = accounts.id THEN transactions.amountCents ELSE 0 END), 0) AS incomeCents,
            COALESCE(SUM(CASE WHEN transactions.type = 'EXPENSE' AND transactions.accountId = accounts.id THEN transactions.amountCents ELSE 0 END), 0) AS expenseCents,
            COALESCE(SUM(CASE WHEN transactions.type = 'TRANSFER' AND transactions.toAccountId = accounts.id THEN transactions.amountCents ELSE 0 END), 0) AS transferInCents,
            COALESCE(SUM(CASE WHEN transactions.type = 'TRANSFER' AND transactions.fromAccountId = accounts.id THEN transactions.amountCents ELSE 0 END), 0) AS transferOutCents,
            COALESCE(SUM(CASE WHEN transactions.type = 'BALANCE_ADJUSTMENT' AND transactions.accountId = accounts.id THEN transactions.amountCents ELSE 0 END), 0) AS adjustmentCents
        FROM accounts
        LEFT JOIN transactions ON transactions.deletedAt IS NULL AND (
            transactions.accountId = accounts.id OR
            transactions.fromAccountId = accounts.id OR
            transactions.toAccountId = accounts.id
        )
        WHERE accounts.deletedAt IS NULL
        GROUP BY accounts.id
        ORDER BY accounts.isArchived ASC, accounts.sortOrder ASC, accounts.createdAt ASC
        """
    )
    fun observeAccountBalances(): Flow<List<AccountBalanceRow>>

    @Query("SELECT * FROM accounts WHERE deletedAt IS NULL ORDER BY isArchived ASC, sortOrder ASC, createdAt ASC")
    fun observeAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts ORDER BY isArchived ASC, sortOrder ASC, createdAt ASC")
    suspend fun getAccountsSnapshot(): List<AccountEntity>

    @Query("SELECT * FROM accounts WHERE deletedAt IS NULL AND isArchived = 0 ORDER BY sortOrder ASC, createdAt ASC")
    fun observeActiveAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccount(id: Long): AccountEntity?

    @Query("SELECT * FROM accounts WHERE deletedAt IS NULL AND isArchived = 0 ORDER BY sortOrder ASC, createdAt ASC")
    suspend fun getActiveAccounts(): List<AccountEntity>

    @Query("SELECT COUNT(*) FROM accounts WHERE deletedAt IS NULL")
    suspend fun countActiveAccounts(): Int

    @Query("SELECT COUNT(*) FROM accounts WHERE deletedAt IS NULL AND isArchived = 0")
    suspend fun countAvailableAccounts(): Int

    @Query("SELECT MAX(sortOrder) FROM accounts WHERE deletedAt IS NULL")
    suspend fun getMaxActiveSortOrder(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    @Update
    suspend fun update(account: AccountEntity)

    @Query("UPDATE accounts SET isArchived = :archived, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setArchived(id: Long, archived: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE accounts SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM accounts")
    suspend fun clearAll()
}
