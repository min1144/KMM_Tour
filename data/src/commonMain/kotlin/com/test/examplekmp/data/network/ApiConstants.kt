package com.test.examplekmp.data.network

object ApiConstants {

    const val ETC = "ETC"
    const val APP_NAME = "AppTest"
    const val JSON = "json"
    const val SERVICE_KEY = "xxxxxxxxxxxxx"
    const val DEFAULT_PAGE_NO = "1"

    private const val HOST_API = "https://apis.data.go.kr"
    private const val SUB_DOMAIN = "B551011/KorService1"

    fun appendUrl(url: String): String {
        return "$HOST_API/$SUB_DOMAIN/$url"
    }
}