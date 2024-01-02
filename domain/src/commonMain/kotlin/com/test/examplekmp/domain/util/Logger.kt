package com.test.examplekmp.domain.util


object Logger {

    val default by lazy {
        LoggerProvider.of("usecase")
    }
}