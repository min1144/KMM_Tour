package com.test.examplekmp.presentation.base.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.test.examplekmp.presentation.base.compose.theme.Colors
import com.test.examplekmp.presentation.util.resource.pxToDp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun WindowInsetColor(
    statusBarInset: WindowInsets = WindowInsets(0.dp),
    statusBarColor: Color = Colors.Transparent,
    navigationBarInset: WindowInsets = WindowInsets(0.dp),
    navigationBarColor: Color = Colors.Transparent,
    content: @Composable (BoxScope.() -> Unit)
) {

    CompositionLocalProvider(
        LocalWindowInsetColor provides WindowInsetColorState().apply {
            setStatusBarColor(statusBarColor)
            setNavigationBarColor(navigationBarColor)
        }
    ) {

        StatusBarColor(statusBarInset) {

            NavigationBarColor(
                navigationBarInset,
                content = content
            )
        }
    }
}

@Composable
fun WindowInsetStatusBarColor(
    statusBarColor: Color = Colors.Transparent,
    inScope: Boolean = false
) {
    val scope = rememberCoroutineScope()
    val windowInsetColorState = LocalWindowInsetColor.current
    val oldColor = windowInsetColorState.statusBarColor.value

    windowInsetColorState.setStatusBarColor(statusBarColor, scope)

    if (inScope) {
        OnDisposeScope(windowInsetColorState.statusBarColor, oldColor)
    }
}

@Composable
private fun OnDisposeScope(mutableStateFlow: MutableStateFlow<Color>, color: Color) {
    DisposableEffect(color) {
        onDispose {
            CoroutineScope(Dispatchers.Unconfined).launch {
                mutableStateFlow.emit(color)
            }
        }
    }
}

@Composable
private fun StatusBarColor(
    statusBarInset: WindowInsets,
    content: @Composable (BoxScope.() -> Unit)
) {

    val backgroundColor = LocalWindowInsetColor.current.statusBarColor.collectAsState()
    val shadowColor = LocalWindowInsetColor.current.shadowColor.collectAsState()

    Box(
        modifier = Modifier.background(backgroundColor.value)
    ) {
        val top = statusBarInset.getTop(LocalDensity.current)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(top.pxToDp())
                .background(shadowColor.value)
        )

        Box(
            modifier = Modifier.windowInsetsPadding(statusBarInset),
            content = content
        )
    }
}

@Composable
private fun NavigationBarColor(
    navigationBarInset: WindowInsets,
    content: @Composable (BoxScope.() -> Unit)
) {

    val backgroundColor = LocalWindowInsetColor.current.navigationBarColor.collectAsState()
    val shadowColor = LocalWindowInsetColor.current.shadowColor.collectAsState()

    Box(
        modifier = Modifier.background(backgroundColor.value)
    ) {
        val bottom = navigationBarInset.getBottom(LocalDensity.current)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottom.pxToDp())
                .background(shadowColor.value)
        )

        Box(
            modifier = Modifier.windowInsetsPadding(navigationBarInset),
            content = content
        )
    }
}

val LocalWindowInsetColor = staticCompositionLocalOf { WindowInsetColorState() }

data class WindowInsetColorState(
    val statusBarColor: MutableStateFlow<Color> = MutableStateFlow(Colors.Transparent),
    val navigationBarColor: MutableStateFlow<Color> = MutableStateFlow(Colors.Transparent),
    val shadowColor: MutableStateFlow<Color> = MutableStateFlow(Colors.Transparent)
) {

    fun setStatusBarColor(color: Color, scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)) {
        scope.launch {
            statusBarColor.emit(color)
        }
    }

    fun setNavigationBarColor(color: Color, scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)) {
        scope.launch {
            navigationBarColor.emit(color)
        }
    }
}