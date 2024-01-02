package com.test.examplekmp.android

import android.app.Application
import com.test.examplekmp.AndroidPlatformUtil
import com.test.examplekmp.android.util.AndroidPlatform
import com.test.examplekmp.di.initKoin
import com.test.examplekmp.initApp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidPlatformUtil.platform = AndroidPlatform(applicationContext)

        initKoin(
            appDeclaration = {
                androidContext(this@App)
            },
            platformModule = module {  }
        )

        initApp()
    }
}