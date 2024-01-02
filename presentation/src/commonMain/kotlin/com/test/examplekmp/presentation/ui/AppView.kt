package com.test.examplekmp.presentation.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.seiko.imageloader.LocalImageLoader
import com.test.examplekmp.domain.util.delegate.AppConfigDelegate
import com.test.examplekmp.presentation.base.LocalViewModelStore
import com.test.examplekmp.presentation.base.ViewModelStore
import com.test.examplekmp.presentation.base.root.ProvideComponentContext
import com.test.examplekmp.presentation.base.root.WindowInsetColor
import com.test.examplekmp.presentation.navigation.RootScreen
import com.test.examplekmp.presentation.util.AppConfig
import com.test.examplekmp.presentation.util.createImageLoader
import com.test.examplekmp.presentation.util.koin.koinContext

@Composable
fun AppView(
    componentContext: ComponentContext ?= null,
) {
    koinContext<AppConfigDelegate>().apply {
        AppConfig.init(appVersion, appVersionCode)
    }

    val root = componentContext ?: LifecycleRegistry().run { DefaultComponentContext(this) }
    val viewModelStore = remember { ViewModelStore() }
    val imageLoader = remember { createImageLoader() }
    CompositionLocalProvider(
            LocalViewModelStore.provides(viewModelStore),
            LocalImageLoader.provides(imageLoader)
        ) {
            ProvideComponentContext(root) {
                WindowInsetColor (
                    statusBarInset = WindowInsets.statusBars,
                    navigationBarInset = WindowInsets.navigationBars
                ) {
                    RootScreen()
                }
        }
    }
}