package com.dodo.accounting.core.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class TimeTest {
    private val zone: ZoneId = ZoneId.of("Asia/Shanghai")

    @Test
    fun startOfDay_snapsToLocalMidnight() {
        val noon = LocalDate.of(2026, 3, 15)
            .atTime(12, 34, 56)
            .atZone(zone)
            .toInstant()
            .toEpochMilli()
        val start = startOfDayMillis(noon, zone)
        assertEquals(LocalDate.of(2026, 3, 15), localDateFromMillis(start, zone))
        assertEquals(localDateStartMillis(LocalDate.of(2026, 3, 15), zone), start)
    }

    @Test
    fun startOfMonth_snapsToFirstDay() {
        val mid = localDateStartMillis(LocalDate.of(2026, 3, 15), zone)
        val monthStart = startOfMonthMillis(mid, zone)
        assertEquals(LocalDate.of(2026, 3, 1), localDateFromMillis(monthStart, zone))
    }

    @Test
    fun addMonths_fromMidMonth_goesToNextMonthFirst() {
        val mid = localDateStartMillis(LocalDate.of(2026, 3, 15), zone)
        val next = addMonthsMillis(mid, 1, zone)
        assertEquals(LocalDate.of(2026, 4, 1), localDateFromMillis(next, zone))
    }

    @Test
    fun addDays_preservesCalendarDay() {
        val start = localDateStartMillis(LocalDate.of(2026, 3, 15), zone)
        val next = addDaysMillis(start, 5, zone)
        assertEquals(LocalDate.of(2026, 3, 20), localDateFromMillis(next, zone))
    }

    @Test
    fun localDateRoundTrip() {
        val date = LocalDate.of(2026, 7, 11)
        val millis = localDateStartMillis(date, zone)
        assertEquals(date, localDateFromMillis(millis, zone))
    }
}
