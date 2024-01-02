package com.test.examplekmp.presentation.base.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable


@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalTypography provides Typography.create(),
    ) {
        MaterialTheme(
            typography = AppTheme.typography.asMaterialTypography(),
            colors = MaterialTheme.colors.copy(
                background = Colors.White,
            )
        ) {
            content()
        }
    }
}

object AppTheme {
    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}