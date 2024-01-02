package com.test.examplekmp.util.log

import com.test.examplekmp.presentation.util.log.Log
import com.test.examplekmp.presentation.util.log.LogPrint
import com.test.examplekmp.presentation.util.log.LogPrintFactory
import io.github.aakira.napier.Napier


object Presentation {

    private val factory = object : LogPrintFactory {
        override fun create(tag: String): LogPrint {
            return object : LogPrint {

                override fun d(message: String) {
                    Napier.d(message, tag = tag)
                }

                override fun i(message: String) {
                    Napier.i(message, tag = tag)
                }

                override fun e(message: String) {
                    Napier.e(message, tag = tag)
                }

                override fun v(message: String) {
                    Napier.v(message, tag = tag)
                }
            }
        }
    }

    fun init() {
        Log.init(factory)
    }
}