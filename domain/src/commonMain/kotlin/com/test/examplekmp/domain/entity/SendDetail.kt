package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SendDetail(
    val contentId: String,
    val contentTypeId: String,
    val defaultYN: String,
    val firstImageYN: String,
    val addrinfoYN: String,
    val mapinfoYN: String,
    val overviewYN: String
)