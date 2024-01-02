package com.test.examplekmp.util.log

import com.test.examplekmp.domain.util.Log
import com.test.examplekmp.domain.util.LogPrint
import com.test.examplekmp.domain.util.LogPrintFactory


object Domain {

    private val factory = object : LogPrintFactory {
        override fun create(tag: String): LogPrint {
            val log = NapierLogPrint.create(tag)
            return object : LogPrint {

                override fun d(message: String) {
                    log.d(message)
                }

                override fun i(message: String) {
                    log.i(message)
                }

                override fun e(message: String) {
                    log.e(message)
                }

                override fun v(message: String) {
                    log.v(message)
                }
            }
        }
    }

    fun init() {
        Log.init(factory)
    }
}