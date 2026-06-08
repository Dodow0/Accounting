package com.dodo.accounting.domain.model

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.absoluteValue

@JvmInline
value class Money(val cents: Long) {
    operator fun plus(other: Money): Money = Money(cents + other.cents)
    operator fun minus(other: Money): Money = Money(cents - other.cents)
    operator fun unaryMinus(): Money = Money(-cents)

    fun format(locale: Locale = Locale.CHINA): String {
        val formatter = NumberFormat.getCurrencyInstance(locale)
        return formatter.format(cents / 100.0)
    }

    fun formatPlain(): String {
        val sign = if (cents < 0) "-" else ""
        val abs = cents.absoluteValue
        return "$sign${abs / 100}.${(abs % 100).toString().padStart(2, '0')}"
    }

    companion object {
        val Zero = Money(0)
        const val INVALID_AMOUNT_MESSAGE = "请输入有效金额"

        fun fromMajor(amount: String): Money {
            val normalized = amount.trim()
            if (normalized.isEmpty()) return Zero
            val sign = if (normalized.startsWith("-")) -1 else 1
            val unsigned = normalized.removePrefix("-").removePrefix("+")
            val parts = unsigned.split(".", limit = 2)
            val yuan = parts.getOrNull(0)?.filter(Char::isDigit)?.toLongOrNull() ?: 0L
            val centsPart = parts.getOrNull(1).orEmpty().filter(Char::isDigit)
            val cents = centsPart.padEnd(2, '0').take(2).toLongOrNull() ?: 0L
            return Money(sign * (yuan * 100 + cents))
        }

        fun parseMajorStrict(amount: String): Money? {
            val normalized = amount.trim()
            if (!strictAmountPattern.matches(normalized)) return null

            val sign = if (normalized.startsWith("-")) -1 else 1
            val unsigned = normalized.removePrefix("-").removePrefix("+")
            val parts = unsigned.split(".", limit = 2)
            val yuan = parts.getOrNull(0)
                .orEmpty()
                .ifBlank { "0" }
                .toLongOrNull()
                ?: return null
            val cents = parts.getOrNull(1)
                .orEmpty()
                .padEnd(2, '0')
                .toLongOrNull()
                ?: return null

            return runCatching {
                Money(sign * Math.addExact(Math.multiplyExact(yuan, 100L), cents))
            }.getOrNull()
        }

        fun requireMajorStrict(amount: String): Money =
            parseMajorStrict(amount) ?: throw IllegalArgumentException(INVALID_AMOUNT_MESSAGE)

        private val strictAmountPattern = Regex("""[+-]?(?:\d+(?:\.\d{0,2})?|\.\d{1,2})""")
    }
}
