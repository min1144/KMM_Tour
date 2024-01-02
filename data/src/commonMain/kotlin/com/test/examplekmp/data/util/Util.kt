package com.test.examplekmp.data.util

import com.test.examplekmp.data.network.ApiConstants
import com.test.examplekmp.domain.entity.base.ListType
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

expect fun createNetworkErrorException(throwable: Throwable): Exception

inline fun <reified T : Any> HttpRequestBuilder.applyParametersBuilder(value: T) {
    val jsonObject = Json.encodeToJsonElement(value).jsonObject
    jsonObject.entries.forEach { entry ->
        val entryValue = entry.value
        if (entryValue is JsonPrimitive) {
            parameter(entry.key, entryValue.content)
        }
    }
    getDefaultParam().entries.map {
        parameter(it.key, it.value)
    }
}

inline fun <reified T : Any> HttpRequestBuilder.applyParametersBuilder(value: T, appendParam: Map<String, String>) {
    val jsonObject = Json.encodeToJsonElement(value).jsonObject
    jsonObject.entries.forEach { entry ->
        val entryValue = entry.value
        if (entryValue is JsonPrimitive) {
            parameter(entry.key, entryValue.content)
        }
    }
    appendParam.entries.map {
        parameter(it.key, it.value)
    }
}

fun getDefaultParam(): Map<String, String> {
    return mapOf(
        "pageNo" to ApiConstants.DEFAULT_PAGE_NO,
        "MobileOS" to ApiConstants.ETC,
        "MobileApp" to ApiConstants.APP_NAME,
        "_type" to ApiConstants.JSON,
        "serviceKey" to ApiConstants.SERVICE_KEY,
        "listYN" to ListType.GRID.type
    )
}

