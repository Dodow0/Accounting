package com.dodo.accounting.domain.model

import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.domain.util.hasUnresolvedAmountExpression
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId

fun projectedCategoryBudgetCents(
    categoryId: Long,
    amount: String,
    occurredAt: Long,
    expenseByCategory: List<CategorySummaryRow>,
    editingTransaction: TransactionEntity?
): Long {
    val spentCents = expenseByCategory
        .firstOrNull { it.categoryId == categoryId }
        ?.amountCents ?: 0
    val editingDeduction = editingTransaction
        ?.takeIf {
            it.type == TransactionType.EXPENSE &&
                it.categoryId == categoryId &&
                isSameMonth(it.occurredAt, occurredAt)
        }
        ?.amountCents ?: 0
    val enteredCents = if (hasUnresolvedAmountExpression(amount)) {
        0
    } else {
        Money.parseMajorStrict(amount)?.cents?.coerceAtLeast(0) ?: 0
    }
    return (spentCents - editingDeduction).coerceAtLeast(0) + enteredCents
}

private fun isSameMonth(leftMillis: Long, rightMillis: Long): Boolean {
    return yearMonth(leftMillis) == yearMonth(rightMillis)
}

private fun yearMonth(millis: Long): YearMonth {
    return YearMonth.from(
        Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    )
}
