package com.test.examplekmp.domain.entity.base.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val response: BaseRootResponse<T>
)

@Serializable
data class BaseRootResponse<T>(
    val header: BaseHeaderResponse,
    val body: BaseBodyResponse<T>
)

@Serializable
data class BaseHeaderResponse(
    val resultCode: String?,
    var resultMsg: String?
)

@Serializable
data class BaseBodyResponse<T>(
    val items: BaseResponseItems<T>?,
    val numOfRows: Int = 0,
    val pageNo: Int = 1,
    val totalCount: Int = 0
)

@Serializable
data class BaseResponseItems<T>(
    @SerialName("item")
    val item: List<T> = emptyList()
)