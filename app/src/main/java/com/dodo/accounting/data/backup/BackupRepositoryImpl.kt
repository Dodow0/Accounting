package com.dodo.accounting.data.backup

import androidx.room.withTransaction
import com.dodo.accounting.data.local.AccountingDatabase
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.BudgetEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionTagCrossRef
import com.dodo.accounting.domain.model.BackupPreview
import com.dodo.accounting.domain.repository.BackupRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class BackupRepositoryImpl @Inject constructor(
    private val database: AccountingDatabase
) : BackupRepository {
    private val accountDao = database.accountDao()
    private val categoryDao = database.categoryDao()
    private val tagDao = database.tagDao()
    private val transactionDao = database.transactionDao()
    private val budgetDao = database.budgetDao()
    private val recurringRuleDao = database.recurringRuleDao()

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
        val backupJson: Json = Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }
}

private fun String.csvEscape(): String {
    val escaped = replace("\"", "\"\"")
    return if (contains(",") || contains("\"") || contains("\n")) {
        "\"$escaped\""
    } else {
        escaped
    }
}
