package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SendKeyword(
    val numOfRows: String,
    val arrange: String,
    val keyword: String,
    val contentTypeId: Int,
    val areaCode: String,
)