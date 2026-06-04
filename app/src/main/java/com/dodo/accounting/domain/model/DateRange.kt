package com.dodo.accounting.domain.model

import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek

data class DateRange(
    val startMillis: Long,
    val endMillis: Long,
    val label: String
)

enum class StatsPeriod {
    WEEK,
    MONTH,
    YEAR
}

fun StatsPeriod.rangeContaining(
    date: LocalDate = LocalDate.now(),
    zoneId: ZoneId = ZoneId.systemDefault()
): DateRange {
    val startDate = when (this) {
        StatsPeriod.WEEK -> date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        StatsPeriod.MONTH -> date.withDayOfMonth(1)
        StatsPeriod.YEAR -> date.withDayOfYear(1)
    }
    val endDate = when (this) {
        StatsPeriod.WEEK -> startDate.plusWeeks(1)
        StatsPeriod.MONTH -> startDate.plusMonths(1)
        StatsPeriod.YEAR -> startDate.plusYears(1)
    }
    val label = when (this) {
        StatsPeriod.WEEK -> "${startDate.monthValue}/${startDate.dayOfMonth} - ${endDate.minusDays(1).monthValue}/${endDate.minusDays(1).dayOfMonth}"
        StatsPeriod.MONTH -> "${startDate.year}-${startDate.monthValue.toString().padStart(2, '0')}"
        StatsPeriod.YEAR -> "${startDate.year}"
    }
    return DateRange(
        startMillis = startDate.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        endMillis = endDate.atStartOfDay(zoneId).toInstant().toEpochMilli(),
        label = label
    )
}
