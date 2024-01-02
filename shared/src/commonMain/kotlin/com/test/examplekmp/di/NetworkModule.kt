package com.test.examplekmp.di

import com.test.examplekmp.data.network.ApiService
import com.test.examplekmp.data.network.HttpClientFactory
import com.test.examplekmp.util.log.Log
import org.koin.dsl.module

val networkModule = module {

    single {
        val factory = HttpClientFactory(
            appConfig = get(),
            client = get(),
            host = "apis.data.go.kr/B551011/KorService1",
            logger = Log.createNetworkLogger(),
           )
        ApiService(factory.create())
    }
}