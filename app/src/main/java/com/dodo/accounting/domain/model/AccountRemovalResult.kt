package com.dodo.accounting.domain.model

data class AccountRemovalResult(
    val action: AccountRemovalAction,
    val disabledRecurringRuleCount: Int = 0
)

enum class AccountRemovalAction {
    ARCHIVED,
    DELETED,
    RESTORED,
    UNCHANGED
}
