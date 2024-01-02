package com.test.examplekmp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.test.examplekmp.presentation.base.root.Navigate
import com.test.examplekmp.presentation.ui.main.MainScreen
import com.test.examplekmp.presentation.ui.splash.SplashScreen

sealed class ScreenConfig(
    internal val func: @Composable (ScreenConfig) -> Unit,
) : Parcelable {

    @Parcelize
    data object Splash : ScreenConfig({ SplashScreen() })
    @Parcelize
    data object Main : ScreenConfig({ MainScreen() })
}

@Composable
fun RootScreen(
    navigation: StackNavigation<ScreenConfig> = remember { StackNavigation() },
) {
    CompositionLocalProvider(
        LocalNavigation.provides(navigation)
    ) {
        Navigate(
            source = navigation,
            initialStack = { getInitialStack() },
            handleBackButton = true,
        ) { child ->
            child.func.invoke(child)
        }
    }
}

private fun getInitialStack(): List<ScreenConfig> = listOf(ScreenConfig.Splash)

val LocalNavigation = staticCompositionLocalOf<StackNavigation<ScreenConfig>> {
    StackNavigation()
}
