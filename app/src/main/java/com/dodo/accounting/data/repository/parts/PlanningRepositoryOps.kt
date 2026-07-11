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

internal class PlanningRepositoryOps(
    private val database: AccountingDatabase,
    private val transactionOps: TransactionRepositoryOps
) {
    private val budgetDao = database.budgetDao()
    private val recurringRuleDao = database.recurringRuleDao()
    private val accountDao = database.accountDao()
    private val categoryDao = database.categoryDao()
    private val tagDao = database.tagDao()

    fun observeActiveBudgets(): Flow<List<BudgetEntity>> =
        budgetDao.observeActiveBudgets()
    fun observeMonthlyBudget(): Flow<BudgetEntity?> =
        budgetDao.observeTotalBudget(BudgetPeriod.MONTHLY)
    fun observeRecurringRules(): Flow<List<RecurringRuleEntity>> =
        recurringRuleDao.observeRules()

    suspend fun setMonthlyBudget(amountCents: Long): Long = database.withTransaction {
        require(amountCents > 0) { "预算金额必须大于 0" }
        budgetDao.archiveTotalBudgets(BudgetPeriod.MONTHLY)
        budgetDao.insert(
            BudgetEntity(
                name = "月度总预算",
                period = BudgetPeriod.MONTHLY,
                amountCents = amountCents
            )
        )
    }

    suspend fun setCategoryBudget(categoryId: Long, categoryName: String, amountCents: Long): Long = database.withTransaction {
        require(amountCents > 0) { "分类预算金额必须大于 0" }
        budgetDao.archiveCategoryBudgets(categoryId, BudgetPeriod.MONTHLY)
        budgetDao.insert(
            BudgetEntity(
                name = "${categoryName.trim().ifBlank { "分类" }}预算",
                period = BudgetPeriod.MONTHLY,
                amountCents = amountCents,
                categoryId = categoryId
            )
        )
    }

    suspend fun addRecurringRule(rule: RecurringRuleEntity): Long = database.withTransaction {
        require(rule.name.isNotBlank()) { "周期账单名称不能为空" }
        require(rule.intervalMonths > 0) { "周期月数必须大于 0" }
        TransactionRules.validate(rule.toDraft(rule.nextRunAt))
        requireRuleAccountsAvailable(rule)
        recurringRuleDao.insert(
            rule.copy(
                name = rule.name.trim(),
                merchant = rule.merchant.trim(),
                note = rule.note.trim(),
                intervalMonths = rule.intervalMonths.coerceAtLeast(1),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun setRecurringRuleEnabled(id: Long, enabled: Boolean) = database.withTransaction {
        if (enabled) {
            val rule = recurringRuleDao.getRule(id) ?: error("周期规则不存在")
            requireRuleAccountsAvailable(rule)
        }
        recurringRuleDao.setEnabled(id, enabled)
    }

    suspend fun deleteRecurringRule(id: Long) {
        recurringRuleDao.softDelete(id)
    }

    suspend fun generateDueRecurringTransactions(): RecurringGenerationResult = database.withTransaction {
        val now = System.currentTimeMillis()
        var generated = 0
        var skipped = 0
        recurringRuleDao.getDueRules(now).forEach { rule ->
            if (!rule.toDraft(rule.nextRunAt).isValid() || !rule.hasAvailableAccounts()) {
                recurringRuleDao.update(rule.copy(isEnabled = false, updatedAt = now))
                return@forEach
            }

            var nextRunAt = rule.nextRunAt
            var generatedForRule = 0
            while (nextRunAt <= now && generatedForRule < MAX_RECURRING_RUNS_PER_RULE) {
                transactionOps.insertTransaction(rule.toDraft(nextRunAt))
                nextRunAt = nextRunAt.advanceByMonths(rule.intervalMonths)
                generated += 1
                generatedForRule += 1
            }
            while (nextRunAt <= now) {
                nextRunAt = nextRunAt.advanceByMonths(rule.intervalMonths)
                skipped += 1
            }
            recurringRuleDao.update(rule.copy(nextRunAt = nextRunAt, updatedAt = now))
        }
        RecurringGenerationResult(generatedCount = generated, skippedCount = skipped)
    }

    suspend fun ensureSeedData() = database.withTransaction {
        if (accountDao.countActiveAccounts() == 0) {
            accountDao.insertAll(SeedData.accounts)
        }
        if (categoryDao.countActiveCategories() == 0) {
            categoryDao.insertAll(SeedData.categories)
        }
        if (tagDao.countActiveTags() == 0) {
            tagDao.insertAll(SeedData.tags)
        }
    }

    private suspend fun requireRuleAccountsAvailable(rule: RecurringRuleEntity) {
        require(rule.hasAvailableAccounts()) { "周期规则引用的账户不可用" }
    }

    private suspend fun RecurringRuleEntity.hasAvailableAccounts(): Boolean {
        return referencedAccountIds().all { accountId ->
            accountDao.getAccount(accountId)?.let { it.deletedAt == null && !it.isArchived } == true
        }
    }

    private fun RecurringRuleEntity.referencedAccountIds(): List<Long> {
        return listOfNotNull(accountId, fromAccountId, toAccountId).distinct()
    }

    companion object {
        const val MAX_RECURRING_RUNS_PER_RULE = 36
    }
}

internal fun RecurringRuleEntity.toDraft(occurredAt: Long): TransactionDraft =
    TransactionDraft(
        type = transactionType,
        amountCents = amountCents,
        occurredAt = occurredAt,
        accountId = accountId,
        fromAccountId = fromAccountId,
        toAccountId = toAccountId,
        categoryId = categoryId,
        merchant = merchant,
        note = note,
        source = TransactionSource.RECURRING
    )

internal fun TransactionDraft.isValid(): Boolean =
    runCatching { TransactionRules.validate(this) }.isSuccess

internal fun Long.advanceByMonths(intervalMonths: Int): Long {
    val months = intervalMonths.coerceAtLeast(1).toLong()
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .plusMonths(months)
        .toInstant()
        .toEpochMilli()
}
