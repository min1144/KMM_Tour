package com.test.examplekmp.presentation.util.koin

import org.koin.mp.KoinPlatformTools

inline fun <reified T : Any> koinContext(): T = KoinPlatformTools.defaultContext().get().get()