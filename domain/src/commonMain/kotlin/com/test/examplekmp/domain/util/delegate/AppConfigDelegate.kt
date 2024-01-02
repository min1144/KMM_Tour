package com.test.examplekmp.domain.util.delegate

interface AppConfigDelegate {

    val appVersionCode: String

    val appVersion: String

    val debug: Boolean
}