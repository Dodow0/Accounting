package com.dodo.accounting.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dodo.accounting.data.local.entity.BudgetEntity
import com.dodo.accounting.data.local.entity.BudgetPeriod
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query(
        """
        SELECT * FROM budgets
        WHERE deletedAt IS NULL AND isArchived = 0
        ORDER BY period ASC, createdAt ASC
        """
    )
    fun observeActiveBudgets(): Flow<List<BudgetEntity>>

    @Query(
        """
        SELECT * FROM budgets
        WHERE deletedAt IS NULL AND isArchived = 0 AND period = :period AND categoryId IS NULL
        ORDER BY updatedAt DESC
        LIMIT 1
        """
    )
    fun observeTotalBudget(period: BudgetPeriod): Flow<BudgetEntity?>

    @Query("SELECT * FROM budgets ORDER BY createdAt ASC")
    suspend fun getBudgetsSnapshot(): List<BudgetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(budgets: List<BudgetEntity>)

    @Update
    suspend fun update(budget: BudgetEntity)

    @Query(
        """
        UPDATE budgets
        SET isArchived = 1, updatedAt = :updatedAt
        WHERE deletedAt IS NULL AND isArchived = 0 AND period = :period AND categoryId IS NULL
        """
    )
    suspend fun archiveTotalBudgets(period: BudgetPeriod, updatedAt: Long = System.currentTimeMillis())

    @Query(
        """
        UPDATE budgets
        SET isArchived = 1, updatedAt = :updatedAt
        WHERE deletedAt IS NULL AND isArchived = 0 AND period = :period AND categoryId = :categoryId
        """
    )
    suspend fun archiveCategoryBudgets(
        categoryId: Long,
        period: BudgetPeriod,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("UPDATE budgets SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM budgets")
    suspend fun clearAll()
}
