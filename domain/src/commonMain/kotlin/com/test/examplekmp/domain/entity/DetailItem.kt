package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class DetailItem(
    val contentid: String,
    val contenttypeid: String,
    val title: String,
    val createdtime: String,
    val modifiedtime: String,
    val tel: String,
    val telname: String,
    val homepage: String,
    val booktour: String,
    val firstimage: String,
    val firstimage2: String,
    val cpyrhtDivCd: String,
    val addr1: String,
    val addr2: String,
    val zipcode: String,
    val mapx: String,
    val mapy: String,
    val mlevel: String,
    val overview: String
)