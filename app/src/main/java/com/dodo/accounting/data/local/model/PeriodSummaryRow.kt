package com.dodo.accounting.data.local.model

data class PeriodSummaryRow(
    val expenseCents: Long = 0,
    val incomeCents: Long = 0,
    val transferInCents: Long = 0,
    val transferOutCents: Long = 0,
    val adjustmentCents: Long = 0
)
