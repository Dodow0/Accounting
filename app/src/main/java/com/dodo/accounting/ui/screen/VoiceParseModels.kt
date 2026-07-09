package com.dodo.accounting.ui.screen

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryEntity

internal data class VoiceEntryParseResult(
    val draft: EntryPrefillDraft,
    val learnedHint: String? = null
)

internal data class VoiceResolved<T>(
    val value: T,
    val source: VoiceResolutionSource,
    val message: String,
    val displayValue: String = ""
)

internal enum class VoiceResolutionSource {
    DIRECT,
    KEYWORD,
    HISTORY,
    DEFAULT,
    MISSING
}

internal data class VoiceHistoryHint(
    val category: CategoryEntity?,
    val account: AccountEntity?,
    val merchant: String,
    val reason: String
)

internal data class VoiceAmountResolution(
    val amount: String
)

internal val incomeVoiceKeywords = listOf("收入", "工资", "到账", "入账", "收到", "报销", "奖金", "退款", "兼职", "收益", "红包")
