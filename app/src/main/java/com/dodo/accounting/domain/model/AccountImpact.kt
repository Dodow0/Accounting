package com.dodo.accounting.domain.model

data class AccountImpact(
    val accountId: Long,
    val deltaCents: Long
)
