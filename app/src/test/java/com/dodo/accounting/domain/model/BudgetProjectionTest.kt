package com.dodo.accounting.domain.model

import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.CategorySummaryRow
import java.time.LocalDate
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Test

class BudgetProjectionTest {
    @Test
    fun projectionDeductsEditingTransactionOnlyInEntryMonth() {
        val categoryId = 7L
        val january = millis(2026, 1, 10)
        val february = millis(2026, 2, 10)
        val editing = TransactionEntity(
            type = TransactionType.EXPENSE,
            amountCents = 2_000,
            occurredAt = january,
            accountId = 1,
            categoryId = categoryId
        )

        val januaryProjection = projectedCategoryBudgetCents(
            categoryId = categoryId,
            amount = "5",
            occurredAt = january,
            expenseByCategory = listOf(CategorySummaryRow(categoryId, "餐饮", 5_000)),
            editingTransaction = editing
        )
        val februaryProjection = projectedCategoryBudgetCents(
            categoryId = categoryId,
            amount = "5",
            occurredAt = february,
            expenseByCategory = listOf(CategorySummaryRow(categoryId, "餐饮", 5_000)),
            editingTransaction = editing
        )

        assertEquals(3_500, januaryProjection)
        assertEquals(5_500, februaryProjection)
    }

    private fun millis(year: Int, month: Int, day: Int): Long {
        return LocalDate.of(year, month, day)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}
