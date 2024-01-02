package com.test.examplekmp.domain.entity

import com.test.examplekmp.domain.entity.base.ContentType
import kotlinx.serialization.Serializable

@Serializable
data class Keyword(
    val addr1: String?,
    val addr2: String?,
    val areacode: String?,
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
    val mapx: String?,
    val mapy: String?,
    val mlevel: String?,
    val modifiedtime: String?,
    val sigungucode: String?,
    val tel: String?,
    val title: String?
) {

    val areaText: String
        get() {
            return when(areacode) {
                "1" -> "서울"
                "31" -> "과천"
                else -> "정보없음"
            }
        }

    val contentTypeText: String
        get() {
            return ContentType.fromIdToText(contenttypeid?.toInt() ?: 0)
        }
}