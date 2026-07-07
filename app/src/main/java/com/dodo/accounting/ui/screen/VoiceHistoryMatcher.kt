package com.dodo.accounting.ui.screen

import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.ui.viewmodel.AccountingUiState

internal object VoiceHistoryMatcher {
    fun findHint(
        text: String,
        uiState: AccountingUiState,
        type: TransactionType
    ): VoiceHistoryHint? {
        if (type == TransactionType.TRANSFER) return null
        return uiState.recentTransactions
            .asSequence()
            .filter { it.transaction.deletedAt == null && it.transaction.type == type }
            .mapNotNull { item -> item.toHistoryCandidate(text) }
            .maxByOrNull { it.score }
            ?.takeIf { it.score >= 36 }
            ?.let { candidate ->
                VoiceHistoryHint(
                    category = candidate.item.category,
                    account = candidate.item.account,
                    merchant = candidate.item.transaction.merchant,
                    reason = "根据历史流水“${candidate.phrase}”匹配"
                )
            }
    }
}

private data class VoiceHistoryCandidate(
    val item: TransactionWithDetails,
    val phrase: String,
    val score: Int
)

private fun TransactionWithDetails.toHistoryCandidate(text: String): VoiceHistoryCandidate? {
    val phrases = buildList {
        add(transaction.merchant)
        category?.name?.let(::add)
        tags.forEach { add(it.name) }
        addAll(transaction.note.voiceHistoryTokens())
    }
        .map { it.trim() }
        .filter { it.length >= 2 }
        .distinct()

    return phrases
        .mapNotNull { phrase ->
            val score = when {
                text.contains(phrase, ignoreCase = true) -> 50 + phrase.length
                phrase.contains(text.withoutVoiceNoise(), ignoreCase = true) -> 36 + phrase.length
                else -> 0
            }
            if (score > 0) VoiceHistoryCandidate(this, phrase, score) else null
        }
        .maxByOrNull { it.score }
}

private fun String.withoutVoiceNoise(): String {
    return normalizeVoiceText()
        .replace(VoiceAmountParser.amountSegmentRegex, " ")
        .replace(Regex("""今天|昨天|前天|大前天|明天|早上|上午|中午|下午|晚上|凌晨|夜宵|深夜|收入|支出|花了|用了|转账|转到|转入|转出|到账|入账|收到"""), " ")
        .replace(Regex("""\s+"""), "")
}

private fun String.voiceHistoryTokens(): List<String> {
    val cleaned = withoutVoiceNoise()
    return Regex("""[\p{L}\p{N}]{2,}""")
        .findAll(cleaned)
        .map { it.value }
        .filter { it.length >= 2 }
        .toList()
}
