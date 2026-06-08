package com.dodo.accounting.domain.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.ArrayDeque

fun handleAmountKey(current: String, key: String): String {
    return when (key) {
        "⌫" -> current.dropLast(1)
        "=" -> normalizedAmountInput(current)
        "+", "-", "×", "÷" -> appendOperator(current, key)
        "." -> appendDecimalPoint(current)
        else -> appendDigit(current, key)
    }
}

fun normalizedAmountInput(input: String): String {
    val normalized = input
        .replace('×', '*')
        .replace('÷', '/')
        .trim()

    return evaluateAmountExpression(normalized)
        ?.stripTrailingZeros()
        ?.toPlainString()
        ?: input
}

fun hasUnresolvedAmountExpression(input: String): Boolean {
    val trimmed = input.trim()
    if (trimmed.isBlank()) return false
    return trimmed.any { it in setOf('+', '*', '/', '×', '÷') } || trimmed.drop(1).contains("-")
}

private fun appendOperator(current: String, operator: String): String {
    if (current.isBlank()) {
        return if (operator == "-") "-" else current
    }

    val trimmed = current.trimEnd()
    val last = trimmed.lastOrNull()
    val operatorChars = setOf('+', '-', '×', '÷', '*', '/')
    return if (last in operatorChars) {
        trimmed.dropLast(1) + operator
    } else {
        trimmed + operator
    }
}

private fun appendDecimalPoint(current: String): String {
    val lastNumber = current.split("+", "-", "×", "÷", "*", "/").lastOrNull().orEmpty()
    return if (lastNumber.contains(".")) current else current + "."
}

private fun appendDigit(current: String, digit: String): String {
    val lastNumber = current.split("+", "-", "×", "÷", "*", "/").lastOrNull().orEmpty()
    return if (lastNumber == "0") {
        current.dropLast(1) + digit
    } else {
        current + digit
    }
}

private fun evaluateAmountExpression(expression: String): BigDecimal? {
    val tokens = tokenizeExpression(expression) ?: return null
    if (tokens.isEmpty()) return null

    val values = ArrayDeque<BigDecimal>()
    val operators = ArrayDeque<Char>()

    fun applyOperator(): Boolean {
        if (values.size < 2 || operators.isEmpty()) return false
        val right = values.removeLast()
        val left = values.removeLast()
        val result = when (operators.removeLast()) {
            '+' -> left + right
            '-' -> left - right
            '*' -> left * right
            '/' -> if (right.compareTo(BigDecimal.ZERO) == 0) {
                return false
            } else {
                left.divide(right, 8, RoundingMode.HALF_UP)
            }
            else -> return false
        }
        values.addLast(result)
        return true
    }

    tokens.forEach { token ->
        when (token) {
            "+", "-", "*", "/" -> {
                val operator = token.single()
                while (operators.isNotEmpty() && precedence(operators.last()) >= precedence(operator)) {
                    if (!applyOperator()) return null
                }
                operators.addLast(operator)
            }
            else -> values.addLast(token.toBigDecimalOrNull() ?: return null)
        }
    }

    while (operators.isNotEmpty()) {
        if (!applyOperator()) return null
    }

    return values.singleOrNull()?.setScale(2, RoundingMode.HALF_UP)
}

private fun tokenizeExpression(expression: String): List<String>? {
    val tokens = mutableListOf<String>()
    var index = 0

    while (index < expression.length) {
        val char = expression[index]
        when {
            char.isWhitespace() -> index += 1
            char in setOf('+', '*', '/') -> {
                tokens += char.toString()
                index += 1
            }
            char == '-' -> {
                val isUnary = tokens.isEmpty() || tokens.last() in setOf("+", "-", "*", "/")
                if (isUnary) {
                    val start = index
                    index += 1
                    while (index < expression.length && (expression[index].isDigit() || expression[index] == '.')) {
                        index += 1
                    }
                    tokens += expression.substring(start, index)
                } else {
                    tokens += "-"
                    index += 1
                }
            }
            char.isDigit() || char == '.' -> {
                val start = index
                index += 1
                while (index < expression.length && (expression[index].isDigit() || expression[index] == '.')) {
                    index += 1
                }
                tokens += expression.substring(start, index)
            }
            else -> return null
        }
    }

    return tokens
}

private fun precedence(operator: Char): Int = when (operator) {
    '*', '/' -> 2
    '+', '-' -> 1
    else -> 0
}
