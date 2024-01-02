package com.test.examplekmp.di

import com.test.examplekmp.presentation.util.swift.delegate.SwiftDelegateFactory
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.Companion.start(
    swiftDelegateFactory: SwiftDelegateFactory
): KoinApplication = initKoin(platformModule = module {
    single { swiftDelegateFactory }
})