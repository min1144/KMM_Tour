package com.test.examplekmp.util.log

import com.test.examplekmp.getPlatform
import io.github.aakira.napier.Napier

class NapierLogPrint(private val tag: String) {
    fun d(message: String) {
        if (getPlatform().debug) {
            Napier.d(message = message, tag = tag)
        }
    }

    fun i(message: String) {
        if (getPlatform().debug) {
            Napier.i(message = message, tag = tag)
        }
    }

    fun e(message: String) {
        if (getPlatform().debug) {
            Napier.e(message = message, tag = tag)
        }
    }

    fun v(message: String) {
        if (getPlatform().debug) {
            Napier.v(message = message, tag = tag)
        }
    }

    companion object {

        fun create(tag: String): NapierLogPrint {
            return NapierLogPrint(tag)
        }
    }
}