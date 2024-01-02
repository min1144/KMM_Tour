package com.test.examplekmp.android.util

import android.content.Context
import com.test.examplekmp.Platform

class AndroidPlatform(context: Context) : Platform {

    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    override val appVersionCode: String = "1"

    override val debug: Boolean = true
}