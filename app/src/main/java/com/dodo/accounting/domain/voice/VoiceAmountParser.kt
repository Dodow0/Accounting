package com.dodo.accounting.domain.voice

import java.math.BigDecimal

internal object VoiceAmountParser {
    val amountSegmentRegex = Regex("""[零〇一二两三四五六七八九十百千万亿点块元毛角分0-9.]+""")

    fun extractAmount(text: String): String {
        val normalized = text.normalizeVoiceText()
        return amountSegmentRegex
            .findAll(normalized)
            .map { it.value }
            .filter { it.isLikelyAmountSegment() }
            .mapNotNull { parseAmountSegment(it) }
            .lastOrNull()
            ?.stripTrailingZeros()
            ?.toPlainString()
            .orEmpty()
    }

    fun parseAmountSegment(raw: String): BigDecimal? {
        val text = raw
            .replace("人民币", "")
            .replace("块钱", "块")
            .trim()
        if (text.isBlank()) return null
        text.toBigDecimalOrNull()?.let { return it }

        val currencyIndex = text.indexOfFirst { it in "块元" }
        if (currencyIndex >= 0) {
            val integerPart = text.substring(0, currencyIndex).ifBlank { "零" }
            val fractionPart = text.substring(currencyIndex + 1)
            val integer = integerPart.toBigDecimalOrNull()
                ?: parseChineseInteger(integerPart)?.let { BigDecimal.valueOf(it) }
                ?: return null
            return integer.add(parseChineseFraction(fractionPart))
        }

        if (text.any { it in "毛角分" }) {
            return parseChineseFraction(text)
        }

        val pointIndex = text.indexOf('点')
        if (pointIndex >= 0) {
            val integer = parseChineseInteger(text.substring(0, pointIndex).ifBlank { "零" }) ?: return null
            val decimalDigits = text.substring(pointIndex + 1)
                .mapNotNull { voiceDigitValue(it) }
                .joinToString("")
            if (decimalDigits.isBlank()) return BigDecimal.valueOf(integer)
            return BigDecimal("${integer}.${decimalDigits.take(2)}")
        }

        return parseChineseInteger(text)?.let { BigDecimal.valueOf(it) }
    }
}

internal fun String.normalizeVoiceText(): String {
    return map { char ->
        when (char) {
            in '０'..'９' -> '0' + (char - '０')
            '，', ',' -> '.'
            '圆' -> '元'
            '俩' -> '两'
            else -> char
        }
    }.joinToString("")
}

internal fun String.isLikelyAmountSegment(): Boolean {
    return any { it.isDigit() } ||
        any { it in "十百千万亿点块元毛角分" } ||
        length > 1
}

private fun parseChineseFraction(text: String): BigDecimal {
    if (text.isBlank()) return BigDecimal.ZERO
    val normalized = text.replace('毛', '角')
    val pureDigits = normalized.filter { voiceDigitValue(it) != null }
    if ('角' !in normalized && '分' !in normalized && pureDigits.length == 1) {
        return BigDecimal("0.${voiceDigitValue(pureDigits.first()) ?: 0}")
    }
    val jiao = normalized.substringBefore("角", missingDelimiterValue = "")
        .lastOrNull()
        ?.let { voiceDigitValue(it) }
    val fenSource = when {
        "分" in normalized -> normalized.substringBefore("分").substringAfterLast("角")
        "角" in normalized -> normalized.substringAfter("角")
        else -> normalized
    }
    val fenDigits = fenSource.mapNotNull { voiceDigitValue(it) }
    val tenths = jiao ?: fenDigits.getOrNull(0)
    val hundredths = if (jiao != null) fenDigits.getOrNull(0) else fenDigits.getOrNull(1)
    return BigDecimal("0.${tenths ?: 0}${hundredths ?: 0}")
}

private fun parseChineseInteger(text: String): Long? {
    if (text.isBlank()) return 0L
    val yiParts = text.split("亿", limit = 2)
    if (yiParts.size == 2) {
        val high = parseChineseInteger(yiParts[0]) ?: return null
        val low = parseChineseInteger(yiParts[1]) ?: return null
        return high * 100_000_000L + low
    }
    val wanParts = text.split("万", limit = 2)
    if (wanParts.size == 2) {
        val high = parseChineseInteger(wanParts[0]) ?: return null
        val lowText = wanParts[1]
        val low = if (lowText.length == 1) {
            (voiceDigitValue(lowText.first()) ?: return null).toLong() * 1000L
        } else {
            parseChineseSection(lowText) ?: return null
        }
        return high * 10_000L + low
    }
    return parseChineseSection(text)
}

private fun parseChineseSection(text: String): Long? {
    var result = 0L
    var number = 0L
    var lastUnit = 1L
    text.forEach { char ->
        when (char) {
            '零', '〇' -> number = 0L
            '十' -> {
                result += (if (number == 0L) 1L else number) * 10L
                number = 0L
                lastUnit = 10L
            }
            '百' -> {
                result += (if (number == 0L) 1L else number) * 100L
                number = 0L
                lastUnit = 100L
            }
            '千' -> {
                result += (if (number == 0L) 1L else number) * 1000L
                number = 0L
                lastUnit = 1000L
            }
            else -> {
                val digit = voiceDigitValue(char) ?: return null
                number = digit.toLong()
            }
        }
    }
    if (number > 0L) {
        result += if (lastUnit > 10L) number * (lastUnit / 10L) else number
    }
    return result
}

private fun voiceDigitValue(char: Char): Int? = when (char) {
    '零', '〇' -> 0
    '一' -> 1
    '二', '两' -> 2
    '三' -> 3
    '四' -> 4
    '五' -> 5
    '六' -> 6
    '七' -> 7
    '八' -> 8
    '九' -> 9
    in '0'..'9' -> char - '0'
    else -> null
}
