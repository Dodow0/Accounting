package com.dodo.accounting.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecurringRuleDao {
    @Query(
        """
        SELECT * FROM recurring_rules
        WHERE deletedAt IS NULL
        ORDER BY isEnabled DESC, nextRunAt ASC, createdAt ASC
        """
    )
    fun observeRules(): Flow<List<RecurringRuleEntity>>

    @Query(
        """
        SELECT * FROM recurring_rules
        WHERE deletedAt IS NULL AND isEnabled = 1 AND nextRunAt <= :now
        ORDER BY nextRunAt ASC
        """
    )
    suspend fun getDueRules(now: Long): List<RecurringRuleEntity>

    @Query("SELECT * FROM recurring_rules ORDER BY createdAt ASC")
    suspend fun getRulesSnapshot(): List<RecurringRuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rule: RecurringRuleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rules: List<RecurringRuleEntity>)

    @Update
    suspend fun update(rule: RecurringRuleEntity)

    @Query("UPDATE recurring_rules SET isEnabled = :enabled, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setEnabled(id: Long, enabled: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE recurring_rules SET deletedAt = :deletedAt, updatedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM recurring_rules")
    suspend fun clearAll()
}
