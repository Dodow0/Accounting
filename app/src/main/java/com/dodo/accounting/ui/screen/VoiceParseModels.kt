package com.dodo.accounting.ui.screen

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryEntity

internal data class VoiceEntryParseResult(
    val draft: EntryPrefillDraft,
    val fields: List<VoiceParseField>,
    val learnedHint: String? = null
) {
    val confidence: VoiceParseConfidence
        get() = when {
            fields.any { it.status == VoiceParseFieldStatus.MISSING } -> VoiceParseConfidence.LOW
            fields.any { it.status == VoiceParseFieldStatus.NEEDS_CONFIRM } -> VoiceParseConfidence.MEDIUM
            else -> VoiceParseConfidence.HIGH
        }

    val needsConfirmation: Boolean
        get() = confidence != VoiceParseConfidence.HIGH
}

internal data class VoiceParseField(
    val label: String,
    val value: String,
    val status: VoiceParseFieldStatus,
    val message: String
)

internal enum class VoiceParseFieldStatus {
    CONFIDENT,
    NEEDS_CONFIRM,
    MISSING
}

internal enum class VoiceParseConfidence(val label: String) {
    HIGH("较高"),
    MEDIUM("需确认"),
    LOW("较低")
}

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
    val amount: String,
    val status: VoiceParseFieldStatus,
    val message: String
)

internal fun <T> VoiceResolved<T>.toParseField(label: String, valueLabel: String): VoiceParseField {
    return VoiceParseField(
        label = label,
        value = valueLabel.ifBlank { "未识别" },
        status = when (source) {
            VoiceResolutionSource.DIRECT,
            VoiceResolutionSource.KEYWORD -> VoiceParseFieldStatus.CONFIDENT
            VoiceResolutionSource.HISTORY,
            VoiceResolutionSource.DEFAULT -> VoiceParseFieldStatus.NEEDS_CONFIRM
            VoiceResolutionSource.MISSING -> VoiceParseFieldStatus.MISSING
        },
        message = message
    )
}

internal val incomeVoiceKeywords = listOf("收入", "工资", "到账", "入账", "收到", "报销", "奖金", "退款", "兼职", "收益", "红包")
