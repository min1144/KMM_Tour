package com.test.examplekmp.presentation.util.resource

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter

actual fun LocalDateTime.dateFormat(format: String): String? {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = format
    return NSCalendar.currentCalendar.dateFromComponents(this.toNSDateComponents())?.let {
        dateFormatter.stringFromDate(it)
    }
}
