package com.dodo.accounting.domain.model

import com.dodo.accounting.data.local.entity.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionRulesTest {
    @Test
    fun transferMovesMoneyBetweenTwoAssetAccounts() {
        val draft = TransactionDraft(
            type = TransactionType.TRANSFER,
            amountCents = 5_000,
            fromAccountId = 1,
            toAccountId = 2
        )

        val impacts = TransactionRules.impacts(draft)

        assertEquals(
            listOf(
                AccountImpact(accountId = 1, deltaCents = -5_000),
                AccountImpact(accountId = 2, deltaCents = 5_000)
            ),
            impacts
        )
    }

    @Test
    fun storedValueTopUpDoesNotCountAsExpenseOrIncome() {
        assertFalse(TransactionRules.countsAsExpense(TransactionType.TRANSFER))
        assertFalse(TransactionRules.countsAsIncome(TransactionType.TRANSFER))
    }

    @Test
    fun expenseOnlyImpactsThePayingAssetAccount() {
        val draft = TransactionDraft(
            type = TransactionType.EXPENSE,
            amountCents = 1_280,
            accountId = 7,
            categoryId = 10
        )

        assertEquals(
            listOf(AccountImpact(accountId = 7, deltaCents = -1_280)),
            TransactionRules.impacts(draft)
        )
        assertTrue(TransactionRules.countsAsExpense(TransactionType.EXPENSE))
    }

    @Test
    fun expenseRequiresCategory() {
        val draft = TransactionDraft(
            type = TransactionType.EXPENSE,
            amountCents = 1_000,
            accountId = 1
        )
        val error = assertThrows(IllegalArgumentException::class.java) {
            TransactionRules.validate(draft)
        }
        assertEquals("请选择分类", error.message)
    }

    @Test
    fun transferRequiresDifferentAccounts() {
        val draft = TransactionDraft(
            type = TransactionType.TRANSFER,
            amountCents = 1_000,
            fromAccountId = 3,
            toAccountId = 3
        )

        assertThrows(IllegalArgumentException::class.java) {
            TransactionRules.validate(draft)
        }
    }
}
