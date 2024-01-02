package com.test.examplekmp

interface Platform {
    val name: String
    val appVersionCode: String
    val debug: Boolean
}

expect fun getPlatform(): Platform

expect fun initApp()