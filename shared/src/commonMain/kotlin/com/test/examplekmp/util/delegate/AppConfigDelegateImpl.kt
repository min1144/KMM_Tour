package com.test.examplekmp.util.delegate

import com.test.examplekmp.domain.util.delegate.AppConfigDelegate
import com.test.examplekmp.getPlatform

class AppConfigDelegateImpl : AppConfigDelegate {

    override val appVersionCode: String = getPlatform().appVersionCode

    override val appVersion: String = getPlatform().name

    override val debug: Boolean = getPlatform().debug
}