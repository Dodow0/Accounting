package com.dodo.accounting.ui.screen

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.domain.model.Money
import com.dodo.accounting.ui.viewmodel.AccountingUiState
import java.time.Instant
import java.time.ZoneId

internal class VoiceEntryParser(
    private val nowMillis: () -> Long = { System.currentTimeMillis() },
    private val zoneId: ZoneId = ZoneId.systemDefault()
) {
    fun parse(text: String, uiState: AccountingUiState): EntryPrefillDraft = parseDetailed(text, uiState).draft

    fun parseMany(text: String, uiState: AccountingUiState): List<VoiceEntryParseResult> {
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

    fun parseDetailed(text: String, uiState: AccountingUiState): VoiceEntryParseResult {
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
        val amountResolution = resolveAmount(trimmed, type, accountMatch?.value, uiState)
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
            note = trimmed,
            tagIds = tagIds,
            occurredAt = occurredAtMatch.value
        )

        return VoiceEntryParseResult(
            draft = draft,
            learnedHint = historyHint?.reason,
            fields = buildList {
                add(typeMatch.toParseField("类型", transactionTypeLabel(type)))
                add(amountField(amountResolution))
                if (type == TransactionType.TRANSFER) {
                    add((fromAccountMatch ?: missingMatch()).toParseField("转出账户", fromAccountMatch?.value?.name.orEmpty()))
                    add((toAccountMatch ?: missingMatch()).toParseField("转入账户", toAccountMatch?.value?.name.orEmpty()))
                } else if (type == TransactionType.BALANCE_ADJUSTMENT) {
                    add((accountMatch ?: missingMatch()).toParseField("账户", accountMatch?.value?.name.orEmpty()))
                } else {
                    add((accountMatch ?: missingMatch()).toParseField("账户", accountMatch?.value?.name.orEmpty()))
                    add(categoryMatch.toParseField("分类", categoryMatch.value?.name.orEmpty()))
                }
                add(occurredAtMatch.toParseField("时间", occurredAtMatch.displayValue))
                if (draft.merchant.isNotBlank()) {
                    add(
                        VoiceParseField(
                            label = "商户",
                            value = draft.merchant,
                            status = VoiceParseFieldStatus.CONFIDENT,
                            message = "根据历史流水补全"
                        )
                    )
                }
            }
        )
    }

    fun guideExamples(uiState: AccountingUiState): List<String> {
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
            VoiceEntityMatcher.isBalanceAdjustmentText(text) -> VoiceResolved(
                value = TransactionType.BALANCE_ADJUSTMENT,
                source = VoiceResolutionSource.DIRECT,
                message = "识别到余额调整表达"
            )
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

    private fun resolveAmount(
        text: String,
        type: TransactionType,
        account: AccountEntity?,
        uiState: AccountingUiState
    ): VoiceAmountResolution {
        val rawAmount = extractAmount(text)
        if (rawAmount.isBlank()) {
            return VoiceAmountResolution(amount = "", status = VoiceParseFieldStatus.MISSING, message = "请补充金额，例如“午饭 28”")
        }
        if (type != TransactionType.BALANCE_ADJUSTMENT || !VoiceEntityMatcher.isBalanceTargetText(text)) {
            return VoiceAmountResolution(amount = rawAmount, status = VoiceParseFieldStatus.CONFIDENT, message = "已识别金额")
        }
        val accountId = account?.id
            ?: return VoiceAmountResolution(amount = rawAmount, status = VoiceParseFieldStatus.NEEDS_CONFIRM, message = "目标余额已识别，账户待确认后再核对差额")
        val currentBalance = uiState.accounts.firstOrNull { it.account.id == accountId }?.balanceCents
            ?: return VoiceAmountResolution(amount = rawAmount, status = VoiceParseFieldStatus.NEEDS_CONFIRM, message = "目标余额已识别，当前余额待核对")
        val targetCents = Money.parseMajorStrict(rawAmount)?.cents
            ?: return VoiceAmountResolution(amount = rawAmount, status = VoiceParseFieldStatus.NEEDS_CONFIRM, message = "目标余额待确认")
        return VoiceAmountResolution(
            amount = (targetCents - currentBalance).toPlainAmount(),
            status = VoiceParseFieldStatus.NEEDS_CONFIRM,
            message = "目标余额 $rawAmount，已按当前余额换算差额"
        )
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
}

internal fun extractVoiceAmount(text: String): String = VoiceEntryParser().extractAmount(text)

private fun missingMatch(): VoiceResolved<AccountEntity?> {
    return VoiceResolved(
        value = null,
        source = VoiceResolutionSource.MISSING,
        message = "未识别账户，进入记账后请选择"
    )
}

private fun amountField(resolution: VoiceAmountResolution): VoiceParseField {
    return if (resolution.amount.isBlank()) {
        VoiceParseField("金额", "未识别", VoiceParseFieldStatus.MISSING, resolution.message)
    } else {
        VoiceParseField("金额", resolution.amount, resolution.status, resolution.message)
    }
}

private fun transactionTypeLabel(type: TransactionType): String = when (type) {
    TransactionType.EXPENSE -> "支出"
    TransactionType.INCOME -> "收入"
    TransactionType.TRANSFER -> "转账"
    TransactionType.BALANCE_ADJUSTMENT -> "余额调整"
}

private fun Long.toVoiceMajorAmount(): String {
    val yuan = this / 100
    val cents = kotlin.math.abs(this % 100)
    return if (cents == 0L) yuan.toString() else "$yuan.${cents.toString().padStart(2, '0').trimEnd('0')}"
}

private fun Long.toPlainAmount(): String {
    val sign = if (this < 0) "-" else ""
    val absolute = kotlin.math.abs(this)
    val yuan = absolute / 100
    val cents = absolute % 100
    return if (cents == 0L) "$sign$yuan" else "$sign$yuan.${cents.toString().padStart(2, '0').trimEnd('0')}"
}
