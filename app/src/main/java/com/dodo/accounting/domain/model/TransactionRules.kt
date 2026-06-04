package com.dodo.accounting.domain.model

import com.dodo.accounting.data.local.entity.TransactionType

object TransactionRules {
    fun validate(draft: TransactionDraft) {
        require(draft.amountCents > 0 || draft.type == TransactionType.BALANCE_ADJUSTMENT) {
            "金额必须大于 0"
        }
        when (draft.type) {
            TransactionType.EXPENSE, TransactionType.INCOME -> {
                requireNotNull(draft.accountId) { "收支必须选择资产账户" }
                require(draft.fromAccountId == null && draft.toAccountId == null) { "收支不能设置转出/转入账户" }
            }
            TransactionType.TRANSFER -> {
                requireNotNull(draft.fromAccountId) { "转账必须选择转出账户" }
                requireNotNull(draft.toAccountId) { "转账必须选择转入账户" }
                require(draft.fromAccountId != draft.toAccountId) { "转出账户和转入账户不能相同" }
                require(draft.accountId == null) { "转账不使用普通账户字段" }
            }
            TransactionType.BALANCE_ADJUSTMENT -> {
                requireNotNull(draft.accountId) { "余额校正必须选择资产账户" }
                require(draft.fromAccountId == null && draft.toAccountId == null) { "余额校正不能设置转出/转入账户" }
            }
        }
    }

    fun impacts(draft: TransactionDraft): List<AccountImpact> {
        validate(draft)
        return when (draft.type) {
            TransactionType.EXPENSE -> listOf(AccountImpact(draft.accountId!!, -draft.amountCents))
            TransactionType.INCOME -> listOf(AccountImpact(draft.accountId!!, draft.amountCents))
            TransactionType.TRANSFER -> listOf(
                AccountImpact(draft.fromAccountId!!, -draft.amountCents),
                AccountImpact(draft.toAccountId!!, draft.amountCents)
            )
            TransactionType.BALANCE_ADJUSTMENT -> listOf(AccountImpact(draft.accountId!!, draft.amountCents))
        }
    }

    fun countsAsExpense(type: TransactionType): Boolean = type == TransactionType.EXPENSE

    fun countsAsIncome(type: TransactionType): Boolean = type == TransactionType.INCOME
}
