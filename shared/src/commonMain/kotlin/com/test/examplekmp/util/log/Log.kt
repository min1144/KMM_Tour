package com.test.examplekmp.util.log


object Log {

    fun createNetworkLogger(): io.ktor.client.plugins.logging.Logger {
        return object : io.ktor.client.plugins.logging.Logger {
            override fun log(message: String) {
                Logger.ktor.d(message)
            }
        }
    }
}

object Logger {

    val default by lazy {
        NapierLogPrint.create("default")
    }

    val ktor by lazy {
        NapierLogPrint.create("ktor")
    }
}