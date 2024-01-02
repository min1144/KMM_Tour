package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SendEvent(
    val numOfRows: String,
    val arrange: String,
    val eventStartDate: String,
    val eventEndDate: String
)