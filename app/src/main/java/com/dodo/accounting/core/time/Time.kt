package com.dodo.accounting.core.time

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private fun systemZone(): ZoneId = ZoneId.systemDefault()

fun localDateFromMillis(
    millis: Long,
    zoneId: ZoneId = systemZone()
): LocalDate {
    return Instant.ofEpochMilli(millis).atZone(zoneId).toLocalDate()
}

fun localDateStartMillis(
    date: LocalDate,
    zoneId: ZoneId = systemZone()
): Long {
    return date.atStartOfDay(zoneId).toInstant().toEpochMilli()
}

fun startOfDayMillis(
    millis: Long,
    zoneId: ZoneId = systemZone()
): Long {
    return localDateStartMillis(localDateFromMillis(millis, zoneId), zoneId)
}

fun startOfMonthMillis(
    millis: Long,
    zoneId: ZoneId = systemZone()
): Long {
    val date = localDateFromMillis(millis, zoneId).withDayOfMonth(1)
    return localDateStartMillis(date, zoneId)
}

fun addDaysMillis(
    millis: Long,
    deltaDays: Int,
    zoneId: ZoneId = systemZone()
): Long {
    val date = localDateFromMillis(millis, zoneId).plusDays(deltaDays.toLong())
    return localDateStartMillis(date, zoneId)
}

/**
 * Adds [deltaMonths] to the month of [millis], then snaps to the first day of that month
 * at local midnight — matching the previous Calendar-based behaviour.
 */
fun addMonthsMillis(
    millis: Long,
    deltaMonths: Int,
    zoneId: ZoneId = systemZone()
): Long {
    val date = localDateFromMillis(millis, zoneId)
        .withDayOfMonth(1)
        .plusMonths(deltaMonths.toLong())
    return localDateStartMillis(date, zoneId)
}
