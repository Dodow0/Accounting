package com.dodo.accounting.data.local.model

import androidx.room.Embedded
import com.dodo.accounting.data.local.entity.AccountEntity

data class AccountBalanceRow(
    @Embedded val account: AccountEntity,
    val incomeCents: Long = 0,
    val expenseCents: Long = 0,
    val transferInCents: Long = 0,
    val transferOutCents: Long = 0,
    val adjustmentCents: Long = 0
) {
    val balanceCents: Long
        get() = account.initialBalanceCents +
            incomeCents -
            expenseCents +
            transferInCents -
            transferOutCents +
            adjustmentCents
}
