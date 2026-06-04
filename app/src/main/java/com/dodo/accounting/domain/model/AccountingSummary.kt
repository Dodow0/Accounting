package com.dodo.accounting.domain.model

import com.dodo.accounting.data.local.model.CategorySummaryRow
import com.dodo.accounting.data.local.model.PeriodSummaryRow

data class AccountingSummary(
    val periodLabel: String,
    val period: StatsPeriod,
    val totals: PeriodSummaryRow,
    val expenseByCategory: List<CategorySummaryRow>
)
