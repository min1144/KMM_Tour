package com.test.examplekmp

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val appVersionCode: String = "1"
    override val debug: Boolean = true
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun initApp() {
    Napier.base(DebugAntilog())
}
