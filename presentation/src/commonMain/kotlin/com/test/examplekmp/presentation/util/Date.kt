package com.test.examplekmp.presentation.util

import com.test.examplekmp.presentation.util.resource.dateFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun todayDate(): String {
    val d = Clock.System.now()
    return d.toLocalDateTime(TimeZone.UTC).dateFormat("yyyyMMdd").toString()
}

fun endMonthDate(): String {
    val today = Clock.System.now()
    val futureTime = 90 * 24 * 60 * 60L
    val futureDate = today.plus(futureTime, DateTimeUnit.SECOND)
    return futureDate.toLocalDateTime(TimeZone.UTC).dateFormat("yyyyMMdd").toString()
}

