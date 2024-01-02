package com.test.examplekmp.presentation.util.log

object Logger {

    val screen by lazy {
        LoggerProvider.of("secreen")
    }

    val ui by lazy {
        LoggerProvider.of("ui")
    }

    val viewmodel by lazy {
        LoggerProvider.of("viewmodel")
    }

    val default by lazy {
        LoggerProvider.of("default")
    }
}