package com.test.examplekmp.presentation.ui.main

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.test.examplekmp.presentation.navigation.DefaultMainBottomComponent
import com.test.examplekmp.presentation.ui.MainBottomNavigationScreen

@Composable
fun MainScreen() {
    val root = DefaultMainBottomComponent(
        componentContext = LifecycleRegistry().run { DefaultComponentContext(this) }
    )
    MainBottomNavigationScreen(root)
}