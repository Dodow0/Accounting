package com.dodo.accounting.domain.repository

import com.dodo.accounting.data.local.entity.BudgetEntity
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.domain.model.RecurringGenerationResult
import kotlinx.coroutines.flow.Flow

interface PlanningRepository {
    fun observeActiveBudgets(): Flow<List<BudgetEntity>>
    fun observeMonthlyBudget(): Flow<BudgetEntity?>
    fun observeRecurringRules(): Flow<List<RecurringRuleEntity>>

    suspend fun setMonthlyBudget(amountCents: Long): Long
    suspend fun setCategoryBudget(categoryId: Long, categoryName: String, amountCents: Long): Long
    suspend fun addRecurringRule(rule: RecurringRuleEntity): Long
    suspend fun setRecurringRuleEnabled(id: Long, enabled: Boolean)
    suspend fun deleteRecurringRule(id: Long)
    suspend fun generateDueRecurringTransactions(): RecurringGenerationResult
    suspend fun ensureSeedData()
}
