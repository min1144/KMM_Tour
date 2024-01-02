package com.test.examplekmp.presentation.base.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.value.Value
import com.test.examplekmp.presentation.extension.notNull
import com.test.examplekmp.presentation.navigation.ScreenConfig


class NavOptionsBuilder(
    var launchSingleTop: Boolean = false,
    var inclusive: Boolean = false,
    var popUpTo: ScreenConfig? = null
) {

    fun filer(stack: List<ScreenConfig>, configuration: ScreenConfig): List<ScreenConfig> {

        val list = popUpTo?.let {
            stack.indexOf(it).run {
                if (this >= 0) {
                    // inclusive 의 경우에는 백스택시 popUpTo 화면의 이전 화면으로 이동되어야 함.
                    stack.subList(0, this + (if (inclusive) 0 else 1))
                } else null
            }
        } ?: stack

        return if (launchSingleTop && configuration.notNull()) {
            list.filterNot { it::class == configuration::class } + configuration
        } else {
            list + configuration
        }
    }
}

fun StackNavigation<ScreenConfig>.navigate(
    configuration: ScreenConfig,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}
) = navigate { stack ->
    NavOptionsBuilder().apply(navOptionsBuilder).filer(stack, configuration)
}

val Value<ChildStack<*, *>>.currentConfiguration: ScreenConfig?
    get() = active.configuration as? ScreenConfig

val Value<ChildStack<*, *>>.previousConfiguration: ScreenConfig?
    get() = backStack.lastOrNull()?.configuration as? ScreenConfig