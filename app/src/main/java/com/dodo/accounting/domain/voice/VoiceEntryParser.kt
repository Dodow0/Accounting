package com.dodo.accounting.domain.voice

import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.TransactionType
import java.time.Instant
import java.time.ZoneId

internal class VoiceEntryParser(
    private val nowMillis: () -> Long = { System.currentTimeMillis() },
    private val zoneId: ZoneId = ZoneId.systemDefault()
) {
    fun parse(text: String, uiState: VoiceParseContext): EntryPrefillDraft = parseDetailed(text, uiState).draft

    fun parseMany(text: String, uiState: VoiceParseContext): List<VoiceEntryParseResult> {
        val trimmed = text.trim()
        if (trimmed.isBlank()) return emptyList()
        val contextPrefix = VoiceDateTimeParser.dateContextPrefix(trimmed)
        return VoiceEntrySplitter.split(trimmed)
            .map { segment ->
                val contextualSegment = if (contextPrefix.isNotBlank() && !VoiceDateTimeParser.hasDateTimeHint(segment)) {
                    "$contextPrefix $segment"
                } else {
                    segment
                }
                parseDetailed(contextualSegment, uiState)
            }
    }

    fun parseDetailed(text: String, uiState: VoiceParseContext): VoiceEntryParseResult {
        val trimmed = text.trim()
        val typeMatch = detectType(trimmed)
        val type = typeMatch.value
        val categories = when (type) {
            TransactionType.INCOME -> uiState.incomeCategories
            TransactionType.EXPENSE -> uiState.expenseCategories
            else -> emptyList()
        }
        val historyHint = VoiceHistoryMatcher.findHint(trimmed, uiState, type)
        val categoryMatch = VoiceEntityMatcher.findCategory(trimmed, categories, type, historyHint)
        val accountMatch = if (type == TransactionType.TRANSFER) {
            null
        } else {
            VoiceEntityMatcher.findAccount(trimmed, uiState.activeAccounts, historyHint)
        }
        val fromAccountMatch = if (type == TransactionType.TRANSFER) {
            VoiceEntityMatcher.findTransferSourceAccount(trimmed, uiState.activeAccounts)
        } else {
            null
        }
        val toAccountMatch = if (type == TransactionType.TRANSFER) {
            VoiceEntityMatcher.findTransferTargetAccount(trimmed, uiState.activeAccounts, fromAccountMatch?.value)
        } else {
            null
        }
        val amountResolution = resolveAmount(trimmed)
        val occurredAtMatch = occurredAt(trimmed)
        val tagIds = uiState.tags
            .filter { trimmed.contains(it.name, ignoreCase = true) }
            .map { it.id }
            .toSet()

        val draft = EntryPrefillDraft(
            type = type,
            amount = amountResolution.amount,
            accountId = accountMatch?.value?.id,
            fromAccountId = fromAccountMatch?.value?.id,
            toAccountId = toAccountMatch?.value?.id,
            categoryId = categoryMatch.value?.id,
            merchant = historyHint?.merchant.orEmpty(),
            note = extractExplicitNote(trimmed),
            tagIds = tagIds,
            occurredAt = occurredAtMatch.value
        )

        return VoiceEntryParseResult(
            draft = draft,
            learnedHint = historyHint?.reason
        )
    }

    fun guideExamples(uiState: VoiceParseContext): List<String> {
        val preferredAccount = uiState.activeAccounts.firstOrNull()?.name ?: "支付宝"
        val secondAccount = uiState.activeAccounts.drop(1).firstOrNull()?.name ?: "微信"
        val learnedExamples = uiState.recentTransactions
            .asSequence()
            .filter { it.transaction.deletedAt == null && it.transaction.merchant.isNotBlank() }
            .map { "${it.transaction.merchant} ${it.transaction.amountCents.toVoiceMajorAmount()}" }
            .distinct()
            .take(2)
            .toList()
        return (learnedExamples + listOf(
            "今天 午饭 二十八 $preferredAccount",
            "昨天晚上 打车 36 $secondAccount",
            "工资到账 八千 $preferredAccount",
            "上周五 从$preferredAccount 转到 $secondAccount 五十"
        )).distinct().take(5)
    }

    fun extractAmount(text: String): String = VoiceAmountParser.extractAmount(text)

    private fun detectType(text: String): VoiceResolved<TransactionType> {
        return when {
            VoiceEntityMatcher.isTransferText(text) -> VoiceResolved(
                value = TransactionType.TRANSFER,
                source = VoiceResolutionSource.DIRECT,
                message = "识别到转账关键词"
            )
            incomeVoiceKeywords.any { text.contains(it, ignoreCase = true) } -> VoiceResolved(
                value = TransactionType.INCOME,
                source = VoiceResolutionSource.DIRECT,
                message = "识别到收入关键词"
            )
            else -> VoiceResolved(
                value = TransactionType.EXPENSE,
                source = VoiceResolutionSource.DEFAULT,
                message = "未识别收入或转账，按支出处理"
            )
        }
    }

    private fun resolveAmount(text: String): VoiceAmountResolution {
        val rawAmount = extractAmount(text)
        if (rawAmount.isBlank()) {
            return VoiceAmountResolution(amount = "")
        }
        return VoiceAmountResolution(amount = rawAmount)
    }

    private fun occurredAt(text: String): VoiceResolved<Long> {
        val now = Instant.ofEpochMilli(nowMillis()).atZone(zoneId)
        val date = VoiceDateTimeParser.parseDate(text, now.toLocalDate())
        val time = VoiceDateTimeParser.parseTime(text) ?: now.toLocalTime().withSecond(0).withNano(0)
        val hasExplicitTime = VoiceDateTimeParser.hasDateTimeHint(text)
        return VoiceResolved(
            value = date.atTime(time).atZone(zoneId).toInstant().toEpochMilli(),
            source = if (hasExplicitTime) VoiceResolutionSource.DIRECT else VoiceResolutionSource.DEFAULT,
            message = if (hasExplicitTime) "识别到时间表达" else "使用当前时间",
            displayValue = "$date $time"
        )
    }

    private fun extractExplicitNote(text: String): String {
        val marker = explicitNoteMarkers.firstOrNull { text.contains(it, ignoreCase = true) }
            ?: return ""
        return text.substringAfter(marker)
            .trimStart(' ', '，', ',', '。', ':', '：', '-', '是', '为')
            .trim()
    }
}

internal fun extractVoiceAmount(text: String): String = VoiceEntryParser().extractAmount(text)

private val explicitNoteMarkers = listOf("备注一下", "说明一下", "备注是", "备注为", "说明是", "说明为", "备注", "说明")

private fun Long.toVoiceMajorAmount(): String {
    val yuan = this / 100
    val cents = kotlin.math.abs(this % 100)
    return if (cents == 0L) yuan.toString() else "$yuan.${cents.toString().padStart(2, '0').trimEnd('0')}"
}
