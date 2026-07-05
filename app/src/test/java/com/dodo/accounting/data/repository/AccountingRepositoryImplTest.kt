package com.dodo.accounting.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.dodo.accounting.data.local.AccountingDatabase
import com.dodo.accounting.data.local.SeedData
import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.AccountType
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.RecurringRuleEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.domain.model.AccountRemovalAction
import com.dodo.accounting.domain.model.TransactionDraft
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class AccountingRepositoryImplTest {
    private lateinit var database: AccountingDatabase
    private lateinit var repository: AccountingRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AccountingDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = AccountingRepositoryImpl(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun ensureSeedDataCreatesDefaultAccountsCategoriesAndTags() = runTest {
        repository.ensureSeedData()

        assertEquals(SeedData.accounts.size, repository.observeActiveAccounts().first().size)
        assertEquals(SeedData.categories.size, repository.observeCategories().first().size)
        assertEquals(SeedData.tags.size, repository.observeTags().first().size)
    }

    @Test
    fun accountBalancesAggregateTransactionsAndIgnoreSoftDeletedRows() = runTest {
        repository.ensureSeedData()
        val cash = repository.observeActiveAccounts().first().first { it.name == "现金" }
        val bank = repository.observeActiveAccounts().first().first { it.name == "银行卡" }
        val mealCategory = repository.observeCategories(CategoryKind.EXPENSE).first().first { it.name == "餐饮" }

        repository.addTransaction(
            TransactionDraft(
                type = TransactionType.INCOME,
                amountCents = 10_000,
                accountId = cash.id
            )
        )
        val expenseId = repository.addTransaction(
            TransactionDraft(
                type = TransactionType.EXPENSE,
                amountCents = 2_500,
                accountId = cash.id,
                categoryId = mealCategory.id
            )
        )
        repository.addTransaction(
            TransactionDraft(
                type = TransactionType.TRANSFER,
                amountCents = 1_000,
                fromAccountId = cash.id,
                toAccountId = bank.id
            )
        )

        val balances = repository.observeAccountBalances().first()
        assertEquals(6_500L, balances.first { it.account.id == cash.id }.balanceCents)
        assertEquals(1_000L, balances.first { it.account.id == bank.id }.balanceCents)

        repository.softDeleteTransaction(expenseId)

        val balancesAfterDelete = repository.observeAccountBalances().first()
        assertEquals(9_000L, balancesAfterDelete.first { it.account.id == cash.id }.balanceCents)
        assertTrue(repository.observeRecentTransactions().first().none { it.transaction.id == expenseId })
    }

    @Test
    fun softDeleteAllTransactionsMovesActiveRowsToTrashWithoutPermanentDeletion() = runTest {
        repository.ensureSeedData()
        val cash = repository.observeActiveAccounts().first().first { it.name == "现金" }
        val bank = repository.observeActiveAccounts().first().first { it.name == "银行卡" }

        val incomeId = repository.addTransaction(
            TransactionDraft(
                type = TransactionType.INCOME,
                amountCents = 10_000,
                accountId = cash.id
            )
        )
        val transferId = repository.addTransaction(
            TransactionDraft(
                type = TransactionType.TRANSFER,
                amountCents = 3_000,
                fromAccountId = cash.id,
                toAccountId = bank.id
            )
        )

        val movedCount = repository.softDeleteAllTransactions()

        assertEquals(2, movedCount)
        assertTrue(repository.observeRecentTransactions().first().isEmpty())
        assertEquals(setOf(incomeId, transferId), repository.observeTrash().first().map { it.transaction.id }.toSet())
        assertEquals(0L, repository.observeAccountBalances().first().first { it.account.id == cash.id }.balanceCents)

        repository.restoreTransaction(incomeId)

        assertEquals(1, repository.observeRecentTransactions().first().size)
        assertEquals(1, repository.observeTrash().first().size)
        assertEquals(10_000L, repository.observeAccountBalances().first().first { it.account.id == cash.id }.balanceCents)
    }

    @Test
    fun jsonPreviewCountsBackupContentAndImportReplacesCurrentData() = runTest {
        repository.ensureSeedData()
        val cash = repository.observeActiveAccounts().first().first { it.name == "现金" }
        repository.addTransaction(
            TransactionDraft(
                type = TransactionType.INCOME,
                amountCents = 12_345,
                accountId = cash.id
            )
        )
        val exported = repository.exportJson()
        val preview = repository.previewImportJson(exported)

        assertEquals(SeedData.accounts.size, preview.accountCount)
        assertEquals(SeedData.categories.size, preview.categoryCount)
        assertEquals(SeedData.tags.size, preview.tagCount)
        assertEquals(1, preview.transactionCount)

        repository.addAccount(AccountEntity(name = "临时账户", type = AccountType.CUSTOM))
        repository.addTransaction(
            TransactionDraft(
                type = TransactionType.EXPENSE,
                amountCents = 100,
                accountId = cash.id
            )
        )
        assertTrue(repository.observeActiveAccounts().first().any { it.name == "临时账户" })
        assertEquals(2, repository.observeRecentTransactions().first().size)

        repository.importJson(exported)

        val importedAccounts = repository.observeActiveAccounts().first()
        val importedTransactions = repository.observeRecentTransactions().first()
        assertFalse(importedAccounts.any { it.name == "临时账户" })
        assertEquals(preview.accountCount, importedAccounts.size)
        assertEquals(1, importedTransactions.size)
        assertEquals(12_345L, importedTransactions.single().transaction.amountCents)

        val roundTripPreview = repository.previewImportJson(repository.exportJson())
        assertEquals(preview.accountCount, roundTripPreview.accountCount)
        assertEquals(preview.categoryCount, roundTripPreview.categoryCount)
        assertEquals(preview.tagCount, roundTripPreview.tagCount)
        assertEquals(preview.transactionCount, roundTripPreview.transactionCount)
        assertEquals(preview.budgetCount, roundTripPreview.budgetCount)
        assertEquals(preview.recurringRuleCount, roundTripPreview.recurringRuleCount)
    }

    @Test
    fun deleteAccountWithHistoryArchivesAndPreservesHistoricalAccountName() = runTest {
        val cashId = repository.addAccount(AccountEntity(name = "现金账户", type = AccountType.CASH))
        repository.addAccount(AccountEntity(name = "备用账户", type = AccountType.BANK_CARD))
        repository.addTransaction(
            TransactionDraft(
                type = TransactionType.INCOME,
                amountCents = 10_000,
                accountId = cashId
            )
        )

        val result = repository.deleteAccount(cashId)

        assertEquals(AccountRemovalAction.ARCHIVED, result.action)
        assertTrue(repository.observeActiveAccounts().first().none { it.id == cashId })
        assertEquals("现金账户", repository.observeRecentTransactions().first().single().account?.name)
        assertTrue(repository.observeAccountBalances().first().first { it.account.id == cashId }.account.isArchived)
    }

    @Test
    fun deleteAccountWithoutHistorySoftDeletesIt() = runTest {
        val emptyAccountId = repository.addAccount(AccountEntity(name = "空账户", type = AccountType.CASH))
        repository.addAccount(AccountEntity(name = "保留账户", type = AccountType.BANK_CARD))

        val result = repository.deleteAccount(emptyAccountId)

        assertEquals(AccountRemovalAction.DELETED, result.action)
        assertTrue(repository.observeAccounts().first().none { it.id == emptyAccountId })
        assertTrue(repository.observeAccountBalances().first().none { it.account.id == emptyAccountId })
    }

    @Test
    fun updateAccountChangesEditableFieldsAndPreservesBalanceHistory() = runTest {
        val accountId = repository.addAccount(
            AccountEntity(
                name = "  旧账户  ",
                type = AccountType.CASH,
                initialBalanceCents = 1_000
            )
        )

        repository.updateAccount(
            id = accountId,
            name = "  工资卡  ",
            type = AccountType.BANK_CARD,
            initialBalanceCents = 2_500,
            iconName = "credit_card",
            colorArgb = 0xFF0891B2
        )

        val row = repository.observeAccountBalances().first().single { it.account.id == accountId }
        assertEquals("工资卡", row.account.name)
        assertEquals(AccountType.BANK_CARD, row.account.type)
        assertEquals(2_500L, row.account.initialBalanceCents)
        assertEquals("credit_card", row.account.iconName)
        assertEquals(0xFF0891B2, row.account.colorArgb)
        assertEquals(2_500L, row.balanceCents)
    }

    @Test
    fun deleteAccountRejectsRemovingLastAvailableAccount() = runTest {
        val onlyAccountId = repository.addAccount(AccountEntity(name = "唯一账户", type = AccountType.CASH))

        val result = runCatching { repository.deleteAccount(onlyAccountId) }

        assertTrue(result.isFailure)
        assertEquals("至少保留一个可用账户", result.exceptionOrNull()?.message)
    }

    @Test
    fun archivingAccountDisablesEnabledRecurringRulesForThatAccount() = runTest {
        val cashId = repository.addAccount(AccountEntity(name = "现金账户", type = AccountType.CASH))
        repository.addAccount(AccountEntity(name = "备用账户", type = AccountType.BANK_CARD))
        repository.addRecurringRule(
            RecurringRuleEntity(
                name = "房租",
                transactionType = TransactionType.EXPENSE,
                amountCents = 3_000_00,
                accountId = cashId,
                nextRunAt = System.currentTimeMillis() + 86_400_000
            )
        )

        val result = repository.deleteAccount(cashId)

        assertEquals(AccountRemovalAction.DELETED, result.action)
        assertEquals(1, result.disabledRecurringRuleCount)
        assertFalse(repository.observeRecurringRules().first().single().isEnabled)
    }

    @Test
    fun disabledRecurringRuleCannotBeReEnabledWhenAccountIsArchived() = runTest {
        val cashId = repository.addAccount(AccountEntity(name = "现金账户", type = AccountType.CASH))
        repository.addAccount(AccountEntity(name = "备用账户", type = AccountType.BANK_CARD))
        val ruleId = repository.addRecurringRule(
            RecurringRuleEntity(
                name = "房租",
                transactionType = TransactionType.EXPENSE,
                amountCents = 3_000_00,
                accountId = cashId,
                nextRunAt = System.currentTimeMillis() + 86_400_000
            )
        )
        repository.deleteAccount(cashId)

        val result = runCatching { repository.setRecurringRuleEnabled(ruleId, true) }

        assertTrue(result.isFailure)
        assertEquals("周期规则引用的账户不可用", result.exceptionOrNull()?.message)
        assertFalse(repository.observeRecurringRules().first().single().isEnabled)
    }

    @Test
    fun deleteCategoryClearsTransactionsBudgetsAndRecurringRulesToUncategorized() = runTest {
        val accountId = repository.addAccount(AccountEntity(name = "现金账户", type = AccountType.CASH))
        val categoryId = repository.addCategory(
            CategoryEntity(name = "旧餐饮", kind = CategoryKind.EXPENSE)
        )
        repository.addTransaction(
            TransactionDraft(
                type = TransactionType.EXPENSE,
                amountCents = 1_500,
                accountId = accountId,
                categoryId = categoryId
            )
        )
        repository.setCategoryBudget(categoryId, "旧餐饮", 20_000)
        repository.addRecurringRule(
            RecurringRuleEntity(
                name = "午餐",
                transactionType = TransactionType.EXPENSE,
                amountCents = 1_500,
                accountId = accountId,
                categoryId = categoryId,
                nextRunAt = System.currentTimeMillis() + 86_400_000
            )
        )

        repository.deleteCategory(categoryId)

        val transaction = repository.observeRecentTransactions().first().single()
        assertEquals(null, transaction.transaction.categoryId)
        assertEquals(null, transaction.category)
        val categorySummary = repository.observeExpenseByCategory(0, System.currentTimeMillis() + 86_400_000).first().single()
        assertEquals(null, categorySummary.categoryId)
        assertEquals(null, categorySummary.categoryName)
        assertTrue(repository.observeActiveBudgets().first().none { it.categoryId == categoryId })
        assertEquals(null, repository.observeRecurringRules().first().single().categoryId)
    }

    @Test
    fun recurringGenerationReportsSkippedRunsPastPerRuleCap() = runTest {
        val accountId = repository.addAccount(AccountEntity(name = "现金账户", type = AccountType.CASH))
        val oldRunAt = Instant.now()
            .atZone(ZoneId.systemDefault())
            .minusMonths(40)
            .toInstant()
            .toEpochMilli()
        repository.addRecurringRule(
            RecurringRuleEntity(
                name = "历史月租",
                transactionType = TransactionType.EXPENSE,
                amountCents = 1_000,
                accountId = accountId,
                intervalMonths = 1,
                nextRunAt = oldRunAt
            )
        )

        val result = repository.generateDueRecurringTransactions()

        assertEquals(36, result.generatedCount)
        assertTrue(result.skippedCount > 0)
        assertEquals(36, repository.observeRecentTransactions(limit = 100).first().size)
        assertTrue(repository.observeRecurringRules().first().single().nextRunAt > System.currentTimeMillis())
    }
}
