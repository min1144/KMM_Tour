package com.test.examplekmp.presentation.util.resource

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.dateFormat(format: String): String? =
    DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDateTime())
