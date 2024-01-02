package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SendMap(
    val numOfRows: String,
    val arrange: String,
    val mapX: Double,
    val mapY: Double,
    val radius: Int,
)