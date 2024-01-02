package com.test.examplekmp.presentation.navigation

import com.arkivanov.decompose.router.stack.StackNavigation
import com.test.examplekmp.presentation.base.root.NavOptionsBuilder
import com.test.examplekmp.presentation.base.root.navigate

internal fun StackNavigation<ScreenConfig>.goMain() =
    goMain {
        popUpTo = ScreenConfig.Splash
        inclusive = true
    }

fun StackNavigation<ScreenConfig>.goMain(
    optionsBuilder: NavOptionsBuilder.() -> Unit
) = navigate(ScreenConfig.Main, optionsBuilder)