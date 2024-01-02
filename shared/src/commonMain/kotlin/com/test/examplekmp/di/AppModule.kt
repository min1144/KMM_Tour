package com.test.examplekmp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(appDeclaration: KoinAppDeclaration = {}, platformModule: Module = module {  }) = startKoin {
    appDeclaration()
    modules(platformModule + appModule())
}

fun appModule() = listOf(
    commonModule,
    nativeNetworkModule,
    networkModule,
    repositoryModule,
    useCaseModule,
    delegateModule,
    viewModelModule
)
