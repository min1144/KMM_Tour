package com.test.examplekmp.presentation.util

object AppConfig {

    private const val APP_VERSION = "APP_VERSION"

    private const val APP_VERSION_CODE = "APP_VERSION_CODE"

    private val map = hashMapOf<String, String>()

    private fun putAppVersion(value: String) {
        map[APP_VERSION] = value
    }

    private fun putAppVersionCode(value: String) {
        map[APP_VERSION_CODE] = value
    }

    fun init(appVersion: String, appVersionCode: String) {
        putAppVersion(appVersion)
        putAppVersionCode(appVersionCode)
    }
}