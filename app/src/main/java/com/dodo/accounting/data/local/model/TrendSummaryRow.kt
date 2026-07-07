package com.dodo.accounting.data.local.model

data class TrendSummaryRow(
    val bucketMonth: String?,
    val expenseCents: Long,
    val incomeCents: Long,
    val count: Int
)
