package com.test.examplekmp.presentation.util.log

import io.github.aakira.napier.Napier


private val defaultLogFactory = object : LogPrintFactory {
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

interface LogPrint {
    fun d(message: String)
    fun i(message: String)
    fun e(message: String)
    fun v(message: String)
}

interface LogPrintFactory {
    fun create(tag: String): LogPrint
}

object Log {

    internal var factory = defaultLogFactory

    fun init(factory: LogPrintFactory) {
        Log.factory = factory
    }
}

internal object LoggerProvider {

    private val map = hashMapOf<String, LogPrint>()

    private val factory
        get() = Log.factory

    fun of(tag: String): LogPrint {
        return map.getOrPut(tag) {
            factory.create(tag)
        }
    }
}