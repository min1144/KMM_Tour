package com.test.examplekmp.presentation.extension

fun String?.getThumbnailUrl(): String {
    return this?.replace("http://", "https://").toString()
}

fun String?.getDetailText(): String {
    return this?.ifEmpty { "정보없음" }.toString()
}

fun String.extractUrl(): String {
    val urlRegex = """\"(https?://[^\"]+)\"""".toRegex()
    return urlRegex.find(this)?.groupValues?.get(1) ?: ""
}