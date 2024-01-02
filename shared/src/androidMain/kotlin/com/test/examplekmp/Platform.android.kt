package com.test.examplekmp

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object AndroidPlatformUtil {
    lateinit var platform: Platform
}

actual fun getPlatform(): Platform = AndroidPlatformUtil.platform

actual fun initApp() {
    Napier.base(DebugAntilog())
}