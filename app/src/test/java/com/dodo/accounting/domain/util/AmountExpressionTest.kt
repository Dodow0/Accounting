package com.dodo.accounting.domain.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AmountExpressionTest {
    @Test
    fun normalizesExpressionWithOperatorPrecedence() {
        assertEquals("7", normalizedAmountInput("1+2×3"))
    }

    @Test
    fun normalizesDivisionWithTwoDecimalRounding() {
        assertEquals("3.33", normalizedAmountInput("10÷3"))
    }

    @Test
    fun keepsOriginalInputWhenExpressionCannotBeResolved() {
        assertEquals("10÷0", normalizedAmountInput("10÷0"))
    }

    @Test
    fun detectsUnresolvedOperators() {
        assertTrue(hasUnresolvedAmountExpression("12+"))
        assertTrue(hasUnresolvedAmountExpression("12-3"))
        assertFalse(hasUnresolvedAmountExpression("-3"))
        assertFalse(hasUnresolvedAmountExpression("12.30"))
    }

    @Test
    fun keypadReplacesDuplicateOperatorsAndBackspaces() {
        assertEquals("12+", handleAmountKey("12", "+"))
        assertEquals("12-", handleAmountKey("12+", "-"))
        assertEquals("12", handleAmountKey("123", "⌫"))
    }

    @Test
    fun keypadPreventsDuplicateDecimalInCurrentNumber() {
        assertEquals("1.2", handleAmountKey("1.2", "."))
        assertEquals("1.2+3.", handleAmountKey("1.2+3", "."))
    }
}
