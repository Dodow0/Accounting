package com.dodo.accounting.data.local

import com.dodo.accounting.data.local.entity.AccountEntity
import com.dodo.accounting.data.local.entity.AccountType
import com.dodo.accounting.data.local.entity.CategoryEntity
import com.dodo.accounting.data.local.entity.CategoryKind
import com.dodo.accounting.data.local.entity.TagEntity

object SeedData {
    val accounts = listOf(
        AccountEntity(name = "现金", type = AccountType.CASH, colorArgb = 0xFF16A34A, iconName = "payments", sortOrder = 0),
        AccountEntity(name = "银行卡", type = AccountType.BANK_CARD, colorArgb = 0xFF2563EB, iconName = "credit_card", sortOrder = 1),
        AccountEntity(name = "微信钱包", type = AccountType.THIRD_PARTY_PAYMENT, colorArgb = 0xFF059669, iconName = "chat", sortOrder = 2),
        AccountEntity(name = "支付宝", type = AccountType.THIRD_PARTY_PAYMENT, colorArgb = 0xFF0284C7, iconName = "account_balance_wallet", sortOrder = 3),
        AccountEntity(name = "Apple 账户余额", type = AccountType.DIGITAL_BALANCE, colorArgb = 0xFF52525B, iconName = "phone_iphone", sortOrder = 4),
        AccountEntity(name = "公交卡", type = AccountType.TRANSIT_CARD, colorArgb = 0xFFF97316, iconName = "directions_bus", sortOrder = 5),
        AccountEntity(name = "超市储值卡", type = AccountType.STORED_VALUE_CARD, colorArgb = 0xFF7C3AED, iconName = "storefront", sortOrder = 6)
    )

    val categories = listOf(
        CategoryEntity(name = "餐饮", kind = CategoryKind.EXPENSE, colorArgb = 0xFFEA580C, iconName = "restaurant", sortOrder = 0),
        CategoryEntity(name = "交通", kind = CategoryKind.EXPENSE, colorArgb = 0xFF0891B2, iconName = "commute", sortOrder = 1),
        CategoryEntity(name = "购物", kind = CategoryKind.EXPENSE, colorArgb = 0xFFDB2777, iconName = "shopping_bag", sortOrder = 2),
        CategoryEntity(name = "数码服务", kind = CategoryKind.EXPENSE, colorArgb = 0xFF4F46E5, iconName = "devices", sortOrder = 3),
        CategoryEntity(name = "生活缴费", kind = CategoryKind.EXPENSE, colorArgb = 0xFF0F766E, iconName = "receipt_long", sortOrder = 4),
        CategoryEntity(name = "工资", kind = CategoryKind.INCOME, colorArgb = 0xFF16A34A, iconName = "work", sortOrder = 0),
        CategoryEntity(name = "优惠/赠送", kind = CategoryKind.INCOME, colorArgb = 0xFF65A30D, iconName = "redeem", sortOrder = 1),
        CategoryEntity(name = "其他收入", kind = CategoryKind.INCOME, colorArgb = 0xFF2563EB, iconName = "add_card", sortOrder = 2)
    )

    val tags = listOf(
        TagEntity(name = "工作日", colorArgb = 0xFF475569),
        TagEntity(name = "家庭", colorArgb = 0xFF9333EA),
        TagEntity(name = "自动候选", colorArgb = 0xFFB45309)
    )
}
