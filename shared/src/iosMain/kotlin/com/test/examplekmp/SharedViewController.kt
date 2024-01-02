package com.test.examplekmp

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.test.examplekmp.presentation.ui.AppView
import platform.UIKit.UIApplication
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

fun SharedViewController() = ComposeUIViewController {
    val componentContext = DefaultComponentContext(LifecycleRegistry())
    AppView(componentContext)
}

private fun selfStartViewControllerMain() {
    dispatch_async(dispatch_get_main_queue()) {
        UIApplication.sharedApplication.keyWindow?.rootViewController = SharedViewController()
    }
}