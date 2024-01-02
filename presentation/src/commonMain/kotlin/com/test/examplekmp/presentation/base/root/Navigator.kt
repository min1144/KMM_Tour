package com.test.examplekmp.presentation.base.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.StackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigationSource
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable

@Composable
inline fun <reified C : Parcelable> Navigate(
    source: StackNavigationSource<C>,
    noinline initialStack: () -> List<C>,
    modifier: Modifier = Modifier,
    handleBackButton: Boolean = false,
    animation: StackAnimation<C, ComponentContext>? = null,
    noinline content: @Composable (C) -> Unit,
) {
    val componentContext = LocalComponentContext.current
    val stack = remember {
        componentContext.childStack(
            source = source,
            initialStack = initialStack,
            handleBackButton = handleBackButton,
            childFactory = { _, childComponentContext -> childComponentContext },
        )
    }

    CompositionLocalProvider(
        LocalNavigationStack.provides(stack)
    ) {
        Children(
            stack = stack,
            modifier = modifier,
            animation = animation,
        ) { child ->
            ProvideComponentContext(child.instance) {
                content(child.configuration)
            }
        }
    }
}

val LocalNavigationStack = staticCompositionLocalOf<Value<ChildStack<*, *>>?> {
    null
}

val LocalComponentContext: ProvidableCompositionLocal<ComponentContext> =
    staticCompositionLocalOf { error("Root component context was not provided") }

@Composable
fun ProvideComponentContext(componentContext: ComponentContext, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalComponentContext provides componentContext, content = content)
}