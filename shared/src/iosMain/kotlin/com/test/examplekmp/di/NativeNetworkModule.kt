package com.test.examplekmp.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val nativeNetworkModule = module {
    single {
        HttpClient(Darwin) {
            engine {
                configureRequest {
                    setAllowsCellularAccess(true)
                }
            }
        }
    }
}