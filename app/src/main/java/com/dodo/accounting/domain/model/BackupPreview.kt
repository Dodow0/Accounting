package com.dodo.accounting.domain.model

data class BackupPreview(
    val schemaVersion: Int,
    val exportedAt: Long,
    val accountCount: Int,
    val categoryCount: Int,
    val tagCount: Int,
    val transactionCount: Int,
    val budgetCount: Int,
    val recurringRuleCount: Int
)
