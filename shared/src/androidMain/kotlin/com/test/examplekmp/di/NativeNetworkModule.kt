package com.test.examplekmp.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

actual val nativeNetworkModule = module {
    single {
        HttpClient(OkHttp) {
            engine {
                config {
                    retryOnConnectionFailure(true)
                    connectTimeout(10, TimeUnit.SECONDS)
                }
            }
        }
    }
}