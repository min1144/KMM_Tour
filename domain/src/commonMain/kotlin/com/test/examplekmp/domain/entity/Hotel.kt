package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Hotel(
    val addr1: String?,
    val addr2: String?,
    val areacode: String?,
    val benikia: String?,
    val booktour: String?,
    val cat1: String?,
    val cat2: String?,
    val cat3: String?,
    val contentid: String?,
    val contenttypeid: String?,
    val cpyrhtDivCd: String?,
    val createdtime: String?,
    val firstimage: String?,
    val firstimage2: String?,
    val goodstay: String?,
    val hanok: String?,
    val mapx: String?,
    val mapy: String?,
    val mlevel: String?,
    val modifiedtime: String?,
    val sigungucode: String?,
    val tel: String?,
    val title: String?
) {

    val regionText: String
        get() {
            return if(!addr1.isNullOrEmpty() && addr1.length > 2) {
                addr1.substring(0, 2)
            } else {
                ""
            }
        }
}