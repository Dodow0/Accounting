package com.dodo.accounting.ui.viewmodel.actions

import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.domain.model.RecurringGenerationResult
import com.dodo.accounting.domain.repository.PlanningRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal class PlanningActions(
    private val scope: CoroutineScope,
    private val repository: PlanningRepository,
    private val showMessage: (String) -> Unit
) {
    fun setMonthlyBudget(amount: String) {
        scope.launch {
            runCatching { repository.setMonthlyBudget(Money.requireMajorStrict(amount).cents) }
                .onSuccess { showMessage("月度预算已更新") }
                .onFailure { showMessage(it.message ?: "预算设置失败") }
        }
    }

    fun setCategoryBudget(category: CategoryEntity, amount: String) {
        scope.launch {
            runCatching { repository.setCategoryBudget(category.id, category.name, Money.requireMajorStrict(amount).cents) }
                .onSuccess { showMessage("${category.name} 预算已更新") }
                .onFailure { showMessage(it.message ?: "分类预算设置失败") }
        }
    }

    fun addMonthlyRecurringRule(
        name: String,
        type: TransactionType,
        amount: String,
        occurredAt: Long = System.currentTimeMillis(),
        accountId: Long?,
        fromAccountId: Long?,
        toAccountId: Long?,
        categoryId: Long?,
        merchant: String,
        note: String
    ) {
        scope.launch {
            runCatching {
                repository.addRecurringRule(
                    RecurringRuleEntity(
                        name = name.trim(),
                        transactionType = type,
                        amountCents = Money.requireMajorStrict(amount).cents,
                        accountId = if (type == TransactionType.TRANSFER) null else accountId,
                        fromAccountId = if (type == TransactionType.TRANSFER) fromAccountId else null,
                        toAccountId = if (type == TransactionType.TRANSFER) toAccountId else null,
                        categoryId = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) categoryId else null,
                        merchant = if (type == TransactionType.EXPENSE || type == TransactionType.INCOME) merchant else "",
                        note = note,
                        intervalMonths = 1,
                        nextRunAt = occurredAt
                    )
                )
                repository.generateDueRecurringTransactions()
            }.onSuccess { result ->
                showMessage("周期规则已添加${result.toRecurringMessage(prefix = "，")}")
            }.onFailure {
                showMessage(it.message ?: "添加周期规则失败")
            }
        }
    }

    fun runDueRecurringRules() {
        scope.launch {
            runCatching { repository.generateDueRecurringTransactions() }
                .onSuccess { showMessage(it.toRecurringMessage()) }
                .onFailure { showMessage(it.message ?: "生成周期账单失败") }
        }
    }

    fun setRecurringRuleEnabled(id: Long, enabled: Boolean) {
        scope.launch {
            runCatching { repository.setRecurringRuleEnabled(id, enabled) }
                .onSuccess { showMessage(if (enabled) "周期规则已启用" else "周期规则已停用") }
                .onFailure { showMessage(it.message ?: "更新周期规则失败") }
        }
    }

    fun deleteRecurringRule(id: Long) {
        scope.launch {
            runCatching { repository.deleteRecurringRule(id) }
                .onSuccess { showMessage("周期规则已删除") }
                .onFailure { showMessage(it.message ?: "删除周期规则失败") }
        }
    }
}

internal fun RecurringGenerationResult.toRecurringMessage(prefix: String = ""): String {
    val generated = if (generatedCount > 0) "已生成 $generatedCount 条账单" else "没有到期账单"
    val skipped = if (skippedCount > 0) "，已跳过 $skippedCount 条超出上限的过期账单" else ""
    return "$prefix$generated$skipped"
}
