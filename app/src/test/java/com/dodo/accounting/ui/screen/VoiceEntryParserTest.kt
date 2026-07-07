package com.dodo.accounting.ui.screen

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.AccountType
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.TagEntity
import com.dodo.accounting.data.local.entity.TransactionEntity
import com.dodo.accounting.data.local.entity.TransactionType
import com.dodo.accounting.data.local.model.AccountBalanceRow
import com.dodo.accounting.data.local.model.TransactionWithDetails
import com.dodo.accounting.ui.viewmodel.AccountingUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class VoiceEntryParserTest {
    private val zoneId: ZoneId = ZoneId.of("Asia/Shanghai")
    private val fixedNowMillis: Long = LocalDateTime.of(2026, 7, 6, 9, 30)
        .atZone(zoneId)
        .toInstant()
        .toEpochMilli()
    private val parser = VoiceEntryParser(
        nowMillis = { fixedNowMillis },
        zoneId = zoneId
    )

    @Test
    fun parsesExpenseWithChineseAmountEveningTimeAndExactAccount() {
        val draft = parser.parse("昨天晚上用微信吃烧烤二十三块五朋友聚餐", sampleState())

        assertEquals(TransactionType.EXPENSE, draft.type)
        assertEquals("23.5", draft.amount)
        assertEquals(2L, draft.accountId)
        assertEquals(10L, draft.categoryId)
        assertEquals(setOf(100L), draft.tagIds)
        assertEquals(
            LocalDateTime.of(2026, 7, 5, 19, 0),
            draft.occurredAt.toLocalDateTime()
        )
    }

    @Test
    fun parsesTransferWithLastWeekdayAndSourceTargetAccounts() {
        val draft = parser.parse("上周五从支付宝转到招商卡五十", sampleState())

        assertEquals(TransactionType.TRANSFER, draft.type)
        assertEquals("50", draft.amount)
        assertEquals(1L, draft.fromAccountId)
        assertEquals(3L, draft.toAccountId)
        assertEquals(
            LocalDateTime.of(2026, 7, 3, 9, 30),
            draft.occurredAt.toLocalDateTime()
        )
    }

    @Test
    fun parsesIncomeWithoutMisclassifyingFromAsTransfer() {
        val draft = parser.parse("从公司收到工资八千招商卡", sampleState())

        assertEquals(TransactionType.INCOME, draft.type)
        assertEquals("8000", draft.amount)
        assertEquals(3L, draft.accountId)
        assertEquals(20L, draft.categoryId)
    }

    @Test
    fun extractsColloquialChineseAmounts() {
        assertEquals("3200", parser.extractAmount("补一笔三千二"))
        assertEquals("150", parser.extractAmount("买菜一百五"))
        assertEquals("12.5", parser.extractAmount("午饭十二块五"))
        assertEquals("23.5", parser.extractAmount("咖啡23块5"))
        assertEquals("36", parser.extractAmount("昨天打车36"))
    }

    @Test
    fun learnsCategoryAccountAndMerchantFromRecentTransactions() {
        val result = parser.parseDetailed("星巴克二十八", sampleState(includeHistory = true))

        assertEquals("28", result.draft.amount)
        assertEquals(2L, result.draft.accountId)
        assertEquals(10L, result.draft.categoryId)
        assertEquals("星巴克", result.draft.merchant)
        assertEquals(VoiceParseConfidence.MEDIUM, result.confidence)
        assertTrue(result.learnedHint.orEmpty().contains("星巴克"))
        assertTrue(result.fields.any { it.label == "分类" && it.status == VoiceParseFieldStatus.NEEDS_CONFIRM })
    }

    @Test
    fun marksMissingAmountAsLowConfidence() {
        val result = parser.parseDetailed("昨天晚上用微信吃烧烤", sampleState())

        assertEquals(VoiceParseConfidence.LOW, result.confidence)
        assertTrue(result.fields.any { it.label == "金额" && it.status == VoiceParseFieldStatus.MISSING })
    }

    @Test
    fun splitsOneSentenceIntoMultipleEntries() {
        val results = parser.parseMany("今天早餐12，打车35，咖啡18", sampleState())

        assertEquals(3, results.size)
        assertEquals("12", results[0].draft.amount)
        assertEquals(10L, results[0].draft.categoryId)
        assertEquals("35", results[1].draft.amount)
        assertEquals(11L, results[1].draft.categoryId)
        assertEquals("18", results[2].draft.amount)
    }

    @Test
    fun splitsCompactSentenceByRepeatedAmounts() {
        val results = parser.parseMany("早餐12打车35咖啡18", sampleState())

        assertEquals(3, results.size)
        assertEquals(listOf("12", "35", "18"), results.map { it.draft.amount })
    }

    @Test
    fun parsesNaturalTransferKeywords() {
        val draft = parser.parse("从支付宝转给招商卡五十", sampleState())

        assertEquals(TransactionType.TRANSFER, draft.type)
        assertEquals("50", draft.amount)
        assertEquals(1L, draft.fromAccountId)
        assertEquals(3L, draft.toAccountId)
    }

    @Test
    fun parsesBalanceAdjustmentTargetAsDelta() {
        val result = parser.parseDetailed("支付宝余额调整为230", sampleState())

        assertEquals(TransactionType.BALANCE_ADJUSTMENT, result.draft.type)
        assertEquals("30", result.draft.amount)
        assertEquals(1L, result.draft.accountId)
        assertTrue(result.fields.any { it.label == "金额" && it.status == VoiceParseFieldStatus.NEEDS_CONFIRM })
    }

    private fun sampleState(includeHistory: Boolean = false): AccountingUiState {
        val accounts = listOf(
            AccountEntity(id = 1L, name = "支付宝", type = AccountType.THIRD_PARTY_PAYMENT),
            AccountEntity(id = 2L, name = "微信", type = AccountType.THIRD_PARTY_PAYMENT),
            AccountEntity(id = 3L, name = "招商卡", type = AccountType.BANK_CARD)
        )
        val categories = listOf(
            CategoryEntity(id = 10L, name = "餐饮", kind = CategoryKind.EXPENSE, iconName = "restaurant"),
            CategoryEntity(id = 11L, name = "交通", kind = CategoryKind.EXPENSE, iconName = "commute"),
            CategoryEntity(id = 20L, name = "工资", kind = CategoryKind.INCOME, iconName = "work")
        )
        return AccountingUiState(
            activeAccounts = accounts,
            accounts = accounts.map {
                AccountBalanceRow(
                    account = it.copy(initialBalanceCents = if (it.id == 1L) 20_000L else 0L)
                )
            },
            categories = categories,
            tags = listOf(
                TagEntity(id = 100L, name = "朋友")
            ),
            recentTransactions = if (includeHistory) {
                listOf(
                    TransactionWithDetails(
                        transaction = TransactionEntity(
                            id = 1L,
                            type = TransactionType.EXPENSE,
                            amountCents = 3200,
                            occurredAt = fixedNowMillis,
                            accountId = 2L,
                            categoryId = 10L,
                            merchant = "星巴克",
                            note = "星巴克咖啡"
                        ),
                        account = accounts[1],
                        fromAccount = null,
                        toAccount = null,
                        category = categories[0],
                        tags = emptyList()
                    )
                )
            } else {
                emptyList()
            }
        )
    }

    private fun Long.toLocalDateTime(): LocalDateTime {
        return Instant.ofEpochMilli(this).atZone(zoneId).toLocalDateTime()
    }
}
