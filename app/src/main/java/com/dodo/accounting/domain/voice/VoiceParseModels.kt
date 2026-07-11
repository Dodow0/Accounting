package com.dodo.accounting.domain.voice

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.TransactionWithDetails

data class EntryPrefillDraft(
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val accountId: Long? = null,
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val categoryId: Long? = null,
    val merchant: String = "",
    val note: String = "",
    val tagIds: Set<Long> = emptySet(),
    val occurredAt: Long = System.currentTimeMillis()
)

data class VoiceParseContext(
    val activeAccounts: List<AccountEntity> = emptyList(),
    val expenseCategories: List<CategoryEntity> = emptyList(),
    val incomeCategories: List<CategoryEntity> = emptyList(),
    val tags: List<TagEntity> = emptyList(),
    val recentTransactions: List<TransactionWithDetails> = emptyList()
)

data class VoiceEntryParseResult(
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
