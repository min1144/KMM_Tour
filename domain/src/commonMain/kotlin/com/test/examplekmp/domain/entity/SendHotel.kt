package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SendHotel(
    val numOfRows: String,
    val arrange: String,
)