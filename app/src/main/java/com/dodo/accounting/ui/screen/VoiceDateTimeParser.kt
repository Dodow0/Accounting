package com.dodo.accounting.ui.screen

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

internal object VoiceDateTimeParser {
    fun parseDate(text: String, today: LocalDate): LocalDate {
        return when {
            text.contains("大前天") -> today.minusDays(3)
            text.contains("前天") -> today.minusDays(2)
            text.contains("昨天") -> today.minusDays(1)
            text.contains("明天") -> today.plusDays(1)
            text.contains("今天") -> today
            else -> parseWeekday(text, today) ?: today
        }
    }

    fun parseTime(text: String): LocalTime? {
        return when {
            text.contains("凌晨") -> LocalTime.of(1, 0)
            text.contains("早上") || text.contains("上午") || text.contains("早餐") || text.contains("早饭") -> LocalTime.of(8, 0)
            text.contains("中午") || text.contains("午饭") || text.contains("午餐") -> LocalTime.of(12, 0)
            text.contains("下午") -> LocalTime.of(15, 0)
            text.contains("晚上") || text.contains("晚饭") || text.contains("晚餐") -> LocalTime.of(19, 0)
            text.contains("夜宵") || text.contains("深夜") -> LocalTime.of(22, 0)
            else -> null
        }
    }

    fun hasDateTimeHint(text: String): Boolean {
        return dateTimeHints.any { it in text } ||
            Regex("""(?:上)?(?:周|星期|礼拜)[一二三四五六日天]""").containsMatchIn(text)
    }

    fun dateContextPrefix(text: String): String {
        val explicitWeekday = Regex("""(?:上)?(?:周|星期|礼拜)[一二三四五六日天]""").find(text)?.value
        val dateHint = dateTimeHints.firstOrNull { it in text && it !in voiceTimeOnlyHints }
        return listOfNotNull(explicitWeekday ?: dateHint)
            .distinct()
            .joinToString(" ")
    }

    private fun parseWeekday(text: String, today: LocalDate): LocalDate? {
        val match = Regex("""(上)?(?:周|星期|礼拜)([一二三四五六日天])""").find(text) ?: return null
        val target = match.groupValues[2].toDayOfWeek() ?: return null
        val current = today.dayOfWeek.value
        val targetValue = target.value
        val delta = if (match.groupValues[1].isNotEmpty()) {
            targetValue - current - 7
        } else {
            val sameWeekDelta = targetValue - current
            if (sameWeekDelta > 0) sameWeekDelta - 7 else sameWeekDelta
        }
        return today.plusDays(delta.toLong())
    }
}

private fun String.toDayOfWeek(): DayOfWeek? = when (this) {
    "一" -> DayOfWeek.MONDAY
    "二" -> DayOfWeek.TUESDAY
    "三" -> DayOfWeek.WEDNESDAY
    "四" -> DayOfWeek.THURSDAY
    "五" -> DayOfWeek.FRIDAY
    "六" -> DayOfWeek.SATURDAY
    "日", "天" -> DayOfWeek.SUNDAY
    else -> null
}

private val dateTimeHints = listOf(
    "大前天", "前天", "昨天", "今天", "明天",
    "凌晨", "早上", "上午", "早餐", "早饭", "中午", "午饭", "午餐",
    "下午", "晚上", "晚饭", "晚餐", "夜宵", "深夜"
)

private val voiceTimeOnlyHints = listOf(
    "凌晨", "早上", "上午", "早餐", "早饭", "中午", "午饭", "午餐",
    "下午", "晚上", "晚饭", "晚餐", "夜宵", "深夜"
)
