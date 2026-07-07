package com.dodo.accounting.data.repository

import androidx.room.withTransaction
import com.dodo.accounting.data.local.AccountingDatabase
import com.dodo.accounting.data.local.SeedData
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.AccountType
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
import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.domain.model.RecurringGenerationResult
import com.dodo.accounting.domain.model.TransactionDraft
import com.dodo.accounting.domain.model.TransactionRules
import com.dodo.accounting.domain.repository.AccountingRepository
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AccountingRepositoryImpl @Inject constructor(
    private val database: AccountingDatabase
) : AccountingRepository {
    private val accountDao = database.accountDao()
    private val categoryDao = database.categoryDao()
    private val tagDao = database.tagDao()
    private val transactionDao = database.transactionDao()
    private val budgetDao = database.budgetDao()
    private val recurringRuleDao = database.recurringRuleDao()

    override fun observeAccountBalances(): Flow<List<AccountBalanceRow>> = accountDao.observeAccountBalances()
    override fun observeAccounts(): Flow<List<AccountEntity>> = accountDao.observeAccounts()
    override fun observeActiveAccounts(): Flow<List<AccountEntity>> = accountDao.observeActiveAccounts()
    override fun observeCategories(): Flow<List<CategoryEntity>> = categoryDao.observeCategories()
    override fun observeCategories(kind: CategoryKind): Flow<List<CategoryEntity>> = categoryDao.observeCategories(kind)
    override fun observeTags(): Flow<List<TagEntity>> = tagDao.observeTags()
    override fun observeRecentTransactions(limit: Int): Flow<List<TransactionWithDetails>> = transactionDao.observeRecent(limit)
    override fun observeActiveTransactionCount(): Flow<Int> = transactionDao.observeActiveCount()
    override fun searchTransactions(
        query: String,
        type: TransactionType?,
        accountId: Long?,
        startAt: Long?,
        endAt: Long?,
        limit: Int,
        offset: Int
    ): Flow<List<TransactionWithDetails>> = transactionDao.search(query, type, accountId, startAt, endAt, limit, offset)

    override fun observeTrash(limit: Int): Flow<List<TransactionWithDetails>> = transactionDao.observeTrash(limit)
    override fun observePeriodSummary(startAt: Long, endAt: Long): Flow<PeriodSummaryRow> =
        transactionDao.observePeriodSummary(startAt, endAt)

    override fun observeExpenseByCategory(startAt: Long, endAt: Long): Flow<List<CategorySummaryRow>> =
        transactionDao.observeExpenseByCategory(startAt, endAt)

    override fun observeMonthlyTrend(startAt: Long, endAt: Long): Flow<List<TrendSummaryRow>> =
        transactionDao.observeMonthlyTrend(startAt, endAt)

    override fun observeActiveBudgets(): Flow<List<BudgetEntity>> =
        budgetDao.observeActiveBudgets()

    override fun observeMonthlyBudget(): Flow<BudgetEntity?> =
        budgetDao.observeTotalBudget(BudgetPeriod.MONTHLY)

    override fun observeRecurringRules(): Flow<List<RecurringRuleEntity>> =
        recurringRuleDao.observeRules()

    override suspend fun addAccount(account: AccountEntity): Long = database.withTransaction {
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

    override suspend fun updateAccount(
        id: Long,
        name: String,
        type: AccountType,
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
                type = type,
                initialBalanceCents = initialBalanceCents,
                iconName = iconName.ifBlank { existing.iconName },
                colorArgb = colorArgb,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun archiveAccount(id: Long, archived: Boolean): AccountRemovalResult = database.withTransaction {
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

    override suspend fun deleteAccount(id: Long): AccountRemovalResult = database.withTransaction {
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

    override suspend fun addCategory(category: CategoryEntity): Long = database.withTransaction {
        val trimmed = category.name.trim()
        require(trimmed.isNotBlank()) { "分类名称不能为空" }
        val existing = categoryDao.getActiveCategoryByName(trimmed, category.kind)
        require(existing == null) { "分类已存在" }
        val nextSortOrder = (categoryDao.getActiveCategories(category.kind).maxOfOrNull { it.sortOrder } ?: -1) + 1
        categoryDao.insert(
            category.copy(
                name = trimmed,
                sortOrder = nextSortOrder,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun renameCategory(id: Long, name: String) = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "分类名称不能为空" }
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        val sameName = categoryDao.getActiveCategoryByName(trimmed, existing.kind)
        require(sameName == null || sameName.id == id) { "分类名称已存在" }
        categoryDao.update(existing.copy(name = trimmed, updatedAt = System.currentTimeMillis()))
    }

    override suspend fun updateCategory(id: Long, name: String, iconName: String, colorArgb: Long) = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "分类名称不能为空" }
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        val sameName = categoryDao.getActiveCategoryByName(trimmed, existing.kind)
        require(sameName == null || sameName.id == id) { "分类名称已存在" }
        categoryDao.update(
            existing.copy(
                name = trimmed,
                iconName = iconName.ifBlank { existing.iconName },
                colorArgb = colorArgb,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun moveCategory(id: Long, direction: Int) = database.withTransaction {
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        val categories = categoryDao.getActiveCategories(existing.kind)
        val currentIndex = categories.indexOfFirst { it.id == id }
        if (currentIndex == -1) return@withTransaction

        val targetIndex = (currentIndex + direction.coerceIn(-1, 1))
            .coerceIn(0, categories.lastIndex)
        if (targetIndex == currentIndex) return@withTransaction

        val target = categories[targetIndex]
        val now = System.currentTimeMillis()
        categoryDao.update(existing.copy(sortOrder = target.sortOrder, updatedAt = now))
        categoryDao.update(target.copy(sortOrder = existing.sortOrder, updatedAt = now))
    }

    override suspend fun deleteCategory(id: Long) = database.withTransaction {
        val existing = categoryDao.getCategoryById(id) ?: error("分类不存在")
        if (existing.deletedAt != null) return@withTransaction
        val now = System.currentTimeMillis()
        transactionDao.clearCategoryReferences(id, now)
        budgetDao.archiveBudgetsForCategory(id, now)
        recurringRuleDao.clearCategoryReferences(id, now)
        categoryDao.softDelete(id, now)
    }

    override suspend fun addTransaction(draft: TransactionDraft): Long = database.withTransaction {
        insertTransaction(draft)
    }

    override suspend fun updateTransaction(id: Long, draft: TransactionDraft) = database.withTransaction {
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

    override suspend fun softDeleteTransaction(id: Long) {
        transactionDao.softDelete(id)
    }

    override suspend fun softDeleteAllTransactions(): Int = database.withTransaction {
        transactionDao.softDeleteAllActive()
    }

    override suspend fun restoreTransaction(id: Long) {
        transactionDao.restore(id)
    }

    override suspend fun permanentlyDeleteTransaction(id: Long) {
        transactionDao.permanentlyDelete(id)
    }

    override suspend fun clearTrash() = database.withTransaction {
        transactionDao.permanentlyDeleteTrash()
    }

    override suspend fun setMonthlyBudget(amountCents: Long): Long = database.withTransaction {
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

    override suspend fun setCategoryBudget(categoryId: Long, categoryName: String, amountCents: Long): Long = database.withTransaction {
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

    override suspend fun addRecurringRule(rule: RecurringRuleEntity): Long = database.withTransaction {
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

    override suspend fun setRecurringRuleEnabled(id: Long, enabled: Boolean) = database.withTransaction {
        if (enabled) {
            val rule = recurringRuleDao.getRule(id) ?: error("周期规则不存在")
            requireRuleAccountsAvailable(rule)
        }
        recurringRuleDao.setEnabled(id, enabled)
    }

    override suspend fun deleteRecurringRule(id: Long) {
        recurringRuleDao.softDelete(id)
    }

    override suspend fun generateDueRecurringTransactions(): RecurringGenerationResult = database.withTransaction {
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
                insertTransaction(rule.toDraft(nextRunAt))
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

    override suspend fun addTag(name: String): Long = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "标签名称不能为空" }
        val existing = tagDao.getTagByName(trimmed)
        when {
            existing == null -> tagDao.insert(TagEntity(name = trimmed))
            existing.deletedAt != null -> {
                tagDao.update(existing.copy(deletedAt = null, updatedAt = System.currentTimeMillis()))
                existing.id
            }
            else -> error("标签已存在")
        }
    }

    override suspend fun renameTag(id: Long, name: String) = database.withTransaction {
        val trimmed = name.trim()
        require(trimmed.isNotBlank()) { "标签名称不能为空" }
        val existing = tagDao.getTagById(id) ?: error("标签不存在")
        val sameName = tagDao.getTagByName(trimmed)
        require(sameName == null || sameName.id == id) { "标签名称已存在" }
        tagDao.update(existing.copy(name = trimmed, updatedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteTag(id: Long) {
        tagDao.softDelete(id)
    }

    override suspend fun ensureSeedData() = database.withTransaction {
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

    override suspend fun exportJson(): String {
        val payload = BackupPayload(
            exportedAt = System.currentTimeMillis(),
            accounts = accountDao.getAccountsSnapshot(),
            categories = categoryDao.getCategoriesSnapshot(),
            tags = tagDao.getTagsSnapshot(),
            transactions = transactionDao.getTransactionsSnapshot(),
            transactionTags = transactionDao.getTransactionTagRefsSnapshot(),
            budgets = budgetDao.getBudgetsSnapshot(),
            recurringRules = recurringRuleDao.getRulesSnapshot()
        )
        return backupJson.encodeToString(payload)
    }

    override suspend fun exportCsv(): String {
        val rows = transactionDao.getAllActive()
        return buildString {
            appendLine("type,amount,occurredAt,accountId,fromAccountId,toAccountId,categoryId,merchant,note,source")
            rows.forEach { transaction ->
                appendLine(
                    listOf(
                        transaction.type.name,
                        transaction.amountCents.toString(),
                        transaction.occurredAt.toString(),
                        transaction.accountId?.toString().orEmpty(),
                        transaction.fromAccountId?.toString().orEmpty(),
                        transaction.toAccountId?.toString().orEmpty(),
                        transaction.categoryId?.toString().orEmpty(),
                        transaction.merchant,
                        transaction.note,
                        transaction.source.name
                    ).joinToString(",") { it.csvEscape() }
                )
            }
        }
    }

    override suspend fun previewImportJson(content: String): BackupPreview =
        parseBackupPayload(content).toPreview()

    override suspend fun importJson(content: String) {
        val payload = parseBackupPayload(content)

        database.withTransaction {
            transactionDao.clearAllTagRefs()
            transactionDao.clearAll()
            recurringRuleDao.clearAll()
            budgetDao.clearAll()
            categoryDao.clearAll()
            tagDao.clearAll()
            accountDao.clearAll()

            if (payload.accounts.isNotEmpty()) accountDao.insertAll(payload.accounts)
            if (payload.categories.isNotEmpty()) categoryDao.insertAll(payload.categories)
            if (payload.tags.isNotEmpty()) tagDao.insertAll(payload.tags)
            if (payload.transactions.isNotEmpty()) transactionDao.insertAll(payload.transactions)
            if (payload.transactionTags.isNotEmpty()) transactionDao.insertTagRefs(payload.transactionTags)
            if (payload.budgets.isNotEmpty()) budgetDao.insertAll(payload.budgets)
            if (payload.recurringRules.isNotEmpty()) recurringRuleDao.insertAll(payload.recurringRules)
        }
    }

    private fun parseBackupPayload(content: String): BackupPayload {
        val payload = backupJson.decodeFromString<BackupPayload>(content)
        require(payload.schemaVersion == 1) { "暂不支持该备份版本" }
        return payload
    }

    private suspend fun insertTransaction(draft: TransactionDraft): Long {
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

    @Serializable
    private data class BackupPayload(
        val schemaVersion: Int = 1,
        val exportedAt: Long,
        val accounts: List<AccountEntity> = emptyList(),
        val categories: List<CategoryEntity> = emptyList(),
        val tags: List<TagEntity> = emptyList(),
        val transactions: List<TransactionEntity> = emptyList(),
        val transactionTags: List<TransactionTagCrossRef> = emptyList(),
        val budgets: List<BudgetEntity> = emptyList(),
        val recurringRules: List<RecurringRuleEntity> = emptyList()
    ) {
        fun toPreview(): BackupPreview =
            BackupPreview(
                schemaVersion = schemaVersion,
                exportedAt = exportedAt,
                accountCount = accounts.size,
                categoryCount = categories.size,
                tagCount = tags.size,
                transactionCount = transactions.size,
                budgetCount = budgets.size,
                recurringRuleCount = recurringRules.size
            )
    }

    private companion object {
        const val MAX_RECURRING_RUNS_PER_RULE = 36

        val backupJson: Json = Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }
}

private fun RecurringRuleEntity.toDraft(occurredAt: Long): TransactionDraft =
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

private fun TransactionDraft.isValid(): Boolean =
    runCatching { TransactionRules.validate(this) }.isSuccess

private fun Long.advanceByMonths(intervalMonths: Int): Long {
    val months = intervalMonths.coerceAtLeast(1).toLong()
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .plusMonths(months)
        .toInstant()
        .toEpochMilli()
}

private fun String.csvEscape(): String {
    val escaped = replace("\"", "\"\"")
    return if (contains(",") || contains("\"") || contains("\n")) {
        "\"$escaped\""
    } else {
        escaped
    }
}
