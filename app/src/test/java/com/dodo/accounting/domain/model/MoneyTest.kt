package com.dodo.accounting.domain.model

import com.dodo.accounting.domain.util.normalizedAmountInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MoneyTest {
    @Test
    fun strictParserAcceptsPlainAmounts() {
        assertEquals(1_200L, Money.parseMajorStrict("12")?.cents)
        assertEquals(1_230L, Money.parseMajorStrict("12.3")?.cents)
        assertEquals(1_230L, Money.parseMajorStrict("12.30")?.cents)
        assertEquals(50L, Money.parseMajorStrict(".5")?.cents)
        assertEquals(-1L, Money.parseMajorStrict("-0.01")?.cents)
    }

    @Test
    fun strictParserRejectsDirtyAmounts() {
        assertNull(Money.parseMajorStrict("abc12"))
        assertNull(Money.parseMajorStrict("¥12"))
        assertNull(Money.parseMajorStrict("12.345"))
        assertNull(Money.parseMajorStrict("12..3"))
        assertNull(Money.parseMajorStrict(""))
    }

    @Test
    fun strictParserAcceptsNormalizedExpressionResults() {
        assertEquals(700L, Money.parseMajorStrict(normalizedAmountInput("1+2×3"))?.cents)
        assertEquals(333L, Money.parseMajorStrict(normalizedAmountInput("10÷3"))?.cents)
    }
}
