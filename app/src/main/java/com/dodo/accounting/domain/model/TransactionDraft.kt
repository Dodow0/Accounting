package com.dodo.accounting.domain.model

import com.dodo.accounting.data.local.entity.TransactionSource
import com.dodo.accounting.data.local.entity.TransactionType

data class TransactionDraft(
    val type: TransactionType,
    val amountCents: Long,
    val occurredAt: Long = System.currentTimeMillis(),
    val accountId: Long? = null,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val categoryId: Long? = null,
    val merchant: String = "",
    val note: String = "",
    val tagIds: List<Long> = emptyList(),
    val source: TransactionSource = TransactionSource.MANUAL
)
