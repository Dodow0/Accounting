package com.dodo.accounting.domain.voice

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.TransactionType

internal object VoiceEntityMatcher {
    fun findAccount(
        text: String,
        accounts: List<AccountEntity>,
        historyHint: VoiceHistoryHint?
    ): VoiceResolved<AccountEntity>? {
        val preferredSegment = accountUsageMarkers
            .mapNotNull { marker -> text.substringAfter(marker, missingDelimiterValue = "").takeIf { it.isNotBlank() } }
            .firstOrNull()
            ?: text
        return accounts.bestAccountMatch(preferredSegment)
            ?: accounts.bestAccountMatch(text)
            ?: historyHint?.account?.let {
                VoiceResolved(it, VoiceResolutionSource.HISTORY, historyHint.reason)
            }
            ?: accounts.firstOrNull()?.let {
                VoiceResolved(it, VoiceResolutionSource.DEFAULT, "使用默认账户")
            }
    }

    fun findTransferSourceAccount(text: String, accounts: List<AccountEntity>): VoiceResolved<AccountEntity>? {
        val fromSegment = text
            .substringAfter("从", missingDelimiterValue = text)
            .substringBefore("转到")
            .substringBefore("转入")
            .substringBefore("转给")
            .substringBefore("转进")
            .substringBefore("到")
            .substringBefore("转")
        return accounts.bestAccountMatch(fromSegment)
            ?: accounts.bestAccountMatch(text)
            ?: accounts.firstOrNull()?.let {
                VoiceResolved(it, VoiceResolutionSource.DEFAULT, "使用默认转出账户")
            }
    }

    fun findTransferTargetAccount(
        text: String,
        accounts: List<AccountEntity>,
        sourceAccount: AccountEntity?
    ): VoiceResolved<AccountEntity>? {
        val targetSegment = when {
            "转到" in text -> text.substringAfter("转到")
            "转入" in text -> text.substringAfter("转入")
            "转给" in text -> text.substringAfter("转给")
            "转进" in text -> text.substringAfter("转进")
            "到" in text -> text.substringAfterLast("到")
            else -> text
        }
        return accounts
            .filter { it.id != sourceAccount?.id }
            .bestAccountMatch(targetSegment)
            ?: accounts.firstOrNull { it.id != sourceAccount?.id }?.let {
                VoiceResolved(it, VoiceResolutionSource.DEFAULT, "使用默认转入账户")
            }
    }

    fun findCategory(
        text: String,
        categories: List<CategoryEntity>,
        type: TransactionType,
        historyHint: VoiceHistoryHint?
    ): VoiceResolved<CategoryEntity?> {
        categories.firstOrNull { text.contains(it.name, ignoreCase = true) }?.let {
            return VoiceResolved(it, VoiceResolutionSource.DIRECT, "识别到分类名称")
        }
        val hints = if (type == TransactionType.INCOME) incomeCategoryHints else expenseCategoryHints
        categories.firstOrNull { category ->
            hints[category.iconName].orEmpty().any { text.contains(it, ignoreCase = true) }
        }?.let {
            return VoiceResolved(it, VoiceResolutionSource.KEYWORD, "根据关键词匹配分类")
        }
        historyHint?.category?.takeIf { historyCategory ->
            categories.any { it.id == historyCategory.id }
        }?.let {
            return VoiceResolved(it, VoiceResolutionSource.HISTORY, historyHint.reason)
        }
        return VoiceResolved(null, VoiceResolutionSource.MISSING, "未识别分类，进入记账后请选择")
    }

    fun isTransferText(text: String): Boolean {
        return transferVoiceKeywords.any { text.contains(it, ignoreCase = true) } ||
            ("从" in text && transferTargetMarkers.any { it in text })
    }

}

private fun List<AccountEntity>.bestAccountMatch(text: String): VoiceResolved<AccountEntity>? {
    return mapNotNull { account ->
        val match = account.matchScore(text)
        if (match.score > 0) account to match else null
    }
        .maxWithOrNull(compareBy<Pair<AccountEntity, AccountMatchScore>> { it.second.score }.thenByDescending { it.first.name.length })
        ?.let { (account, match) ->
            VoiceResolved(account, match.source, match.message)
        }
}

private data class AccountMatchScore(
    val score: Int,
    val source: VoiceResolutionSource,
    val message: String
)

private fun AccountEntity.matchScore(text: String): AccountMatchScore {
    if (text.isBlank()) return AccountMatchScore(0, VoiceResolutionSource.MISSING, "")
    if (text.contains(name, ignoreCase = true)) {
        return AccountMatchScore(100 + name.length, VoiceResolutionSource.DIRECT, "识别到账户名称")
    }
    val normalizedName = name.replace("账户", "").replace("卡", "")
    if (normalizedName.length >= 2 && text.contains(normalizedName, ignoreCase = true)) {
        return AccountMatchScore(80 + normalizedName.length, VoiceResolutionSource.DIRECT, "识别到账户简称")
    }
    accountVoiceAliases(iconName).forEach { alias ->
        if (text.contains(alias, ignoreCase = true)) {
            return if (name.contains(alias, ignoreCase = true)) {
                AccountMatchScore(70 + alias.length, VoiceResolutionSource.DIRECT, "识别到账户别名")
            } else {
                AccountMatchScore(20 + alias.length, VoiceResolutionSource.KEYWORD, "根据账户图标匹配")
            }
        }
    }
    return AccountMatchScore(0, VoiceResolutionSource.MISSING, "")
}

private fun accountVoiceAliases(iconName: String): List<String> = when (iconName) {
    "payments" -> listOf("现金")
    "credit_card" -> listOf("银行卡", "储蓄卡", "借记卡", "信用卡", "卡里", "花呗", "白条")
    "chat" -> listOf("微信", "支付", "转账")
    "account_balance_wallet", "wallet" -> listOf("钱包", "余额", "支付")
    "phone_iphone" -> listOf("手机", "余额", "电子")
    "directions_bus" -> listOf("公交卡", "交通卡", "公交", "地铁")
    "storefront" -> listOf("储值卡", "会员卡", "门店")
    "savings" -> listOf("储蓄", "存款", "理财")
    "business_center" -> listOf("公司", "工作", "报销")
    else -> emptyList()
}

private val transferVoiceKeywords = listOf("转账", "转到", "转入", "转出", "转给", "转进")
private val transferTargetMarkers = listOf("转到", "转入", "转给", "转进", "转")

private val accountUsageMarkers = listOf("用", "从", "在")

private val expenseCategoryHints = mapOf(
    "restaurant" to listOf("饭", "餐", "午饭", "晚饭", "早餐", "外卖", "咖啡", "奶茶", "饮品", "吃", "烧烤", "火锅"),
    "commute" to listOf("打车", "地铁", "公交", "交通", "车费", "通勤", "高铁", "火车", "机票"),
    "directions_bus" to listOf("公交", "地铁", "车票", "交通卡"),
    "shopping_bag" to listOf("购物", "买", "超市", "衣服", "淘宝", "京东"),
    "storefront" to listOf("门店", "超市", "便利店"),
    "devices" to listOf("数码", "手机", "电脑", "耳机"),
    "phone_iphone" to listOf("话费", "手机", "流量"),
    "home" to listOf("房租", "水电", "居家", "物业", "燃气"),
    "credit_card" to listOf("信用卡", "还款"),
    "receipt_long" to listOf("账单", "缴费")
)

private val incomeCategoryHints = mapOf(
    "work" to listOf("工资", "薪水", "薪资", "工资到账"),
    "redeem" to listOf("红包", "奖励"),
    "add_card" to listOf("入账", "到账"),
    "payments" to listOf("报销", "退款", "收到"),
    "wallet" to listOf("兼职", "收入"),
    "assessment" to listOf("收益", "理财", "分红")
)
