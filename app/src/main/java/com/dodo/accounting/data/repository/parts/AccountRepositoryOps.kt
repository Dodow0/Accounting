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

internal class AccountRepositoryOps(
    private val database: AccountingDatabase
) {
    private val accountDao = database.accountDao()
    private val transactionDao = database.transactionDao()
    private val recurringRuleDao = database.recurringRuleDao()

    fun observeAccountBalances(): Flow<List<AccountBalanceRow>> = accountDao.observeAccountBalances()
    fun observeAccounts(): Flow<List<AccountEntity>> = accountDao.observeAccounts()
    fun observeActiveAccounts(): Flow<List<AccountEntity>> = accountDao.observeActiveAccounts()

    suspend fun addAccount(account: AccountEntity): Long = database.withTransaction {
        val trimmed = account.name.trim()
        require(trimmed.isNotBlank()) { "账户名称不能为空" }
        val nextSortOrder = (accountDao.getMaxActiveSortOrder() ?: -1) + 1
        accountDao.insert(
            account.copy(
                name = trimmed,
                sortOrder = nextSortOrder,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateAccount(
        id: Long,
        name: String,
        initialBalanceCents: Long,
        iconName: String,
        colorArgb: Long
    ) = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "账户名称不能为空" }
        val existing = accountDao.getAccount(id)?.takeIf { it.deletedAt == null } ?: error("账户不存在")
        accountDao.update(
            existing.copy(
                name = trimmed,
                initialBalanceCents = initialBalanceCents,
                iconName = iconName.ifBlank { existing.iconName },
                colorArgb = colorArgb,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun archiveAccount(id: Long, archived: Boolean): AccountRemovalResult = database.withTransaction {
        val existing = accountDao.getAccount(id) ?: error("账户不存在")
        if (existing.deletedAt != null) {
            return@withTransaction AccountRemovalResult(AccountRemovalAction.UNCHANGED)
        }
        if (existing.isArchived == archived) {
            val disabledRules = if (archived) {
                recurringRuleDao.disableRulesForAccount(id)
            } else {
                0
            }
            return@withTransaction AccountRemovalResult(
                action = if (archived) AccountRemovalAction.ARCHIVED else AccountRemovalAction.RESTORED,
                disabledRecurringRuleCount = disabledRules
            )
        }
        if (archived) {
            require(accountDao.countAvailableAccounts() > 1) { "至少保留一个可用账户" }
        }
        val now = System.currentTimeMillis()
        accountDao.setArchived(id, archived, now)
        val disabledRules = if (archived) {
            recurringRuleDao.disableRulesForAccount(id, now)
        } else {
            0
        }
        AccountRemovalResult(
            action = if (archived) AccountRemovalAction.ARCHIVED else AccountRemovalAction.RESTORED,
            disabledRecurringRuleCount = disabledRules
        )
    }

    suspend fun deleteAccount(id: Long): AccountRemovalResult = database.withTransaction {
        val existing = accountDao.getAccount(id) ?: error("账户不存在")
        if (existing.deletedAt != null) {
            return@withTransaction AccountRemovalResult(AccountRemovalAction.UNCHANGED)
        }
        val availableAccountCount = accountDao.countAvailableAccounts()
        if (existing.isArchived) {
            require(availableAccountCount >= 1) { "至少保留一个可用账户" }
        } else {
            require(availableAccountCount > 1) { "至少保留一个可用账户" }
        }

        val now = System.currentTimeMillis()
        val disabledRules = recurringRuleDao.disableRulesForAccount(id, now)
        val hasHistory = transactionDao.countReferencingAccount(id) > 0
        if (hasHistory) {
            accountDao.setArchived(id, true, now)
            AccountRemovalResult(
                action = AccountRemovalAction.ARCHIVED,
                disabledRecurringRuleCount = disabledRules
            )
        } else {
            accountDao.softDelete(id, now)
            AccountRemovalResult(
                action = AccountRemovalAction.DELETED,
                disabledRecurringRuleCount = disabledRules
            )
        }
    }

    suspend fun reorderAccounts(ids: List<Long>) = database.withTransaction {
        val activeAccounts = accountDao.getActiveAccounts()
        val requestedIds = ids.distinct()
        val activeIds = activeAccounts.map { it.id }
        require(requestedIds.size == ids.size && requestedIds.toSet() == activeIds.toSet()) { "账户排序数据已变化，请重试" }

        val byId = activeAccounts.associateBy { it.id }
        val now = System.currentTimeMillis()
        requestedIds.forEachIndexed { index, accountId ->
            val account = byId.getValue(accountId)
            if (account.sortOrder != index) {
                accountDao.update(account.copy(sortOrder = index, updatedAt = now))
            }
        }
    }
}
