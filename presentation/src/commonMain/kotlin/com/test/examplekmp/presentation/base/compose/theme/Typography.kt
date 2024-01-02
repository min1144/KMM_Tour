package com.test.examplekmp.presentation.base.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

internal val LocalTypography = staticCompositionLocalOf {
    val empty = TextStyle()
    Typography(
        h1 = empty,
        h2 = empty,
        h3 = empty,
        h4 = empty,
        h5 = empty,
        body1 = empty,
        body2 = empty,
        caption1 = empty,
        caption2 = empty
    )
}

@Immutable
class Typography constructor(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val h5: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle,
) {

    companion object {

        @Composable
        fun create(): Typography {
            return Typography(
                h1 = createH1TextStyle(),
                h2 = createH2TextStyle(),
                h3 = createH3TextStyle(),
                h4 = createH4TextStyle(),
                h5 = createH5TextStyle(),
                body1 = createBody1TextStyle(),
                body2 = createBody2TextStyle(),
                caption1 = createCaption1TextStyle(),
                caption2 = createCaption2TextStyle()
            )
        }
    }

        fun asMaterialTypography(): androidx.compose.material.Typography {
            return androidx.compose.material.Typography(
                h1 = h1,
                h2 = h2,
                h3 = h3,
                h4 = h4,
                h5 = h5,
                body1 = body1,
                body2 = body2,
                caption = caption1
            )
        }
}

@Composable
private fun Dp.asSp(): TextUnit = with(LocalDensity.current) { this@asSp.toPx().toSp() }

@Composable
internal fun createH1TextStyle() = TextStyle(
    fontSize = 32.dp.asSp(),
    lineHeight = 40.dp.asSp()
).normal()

@Composable
internal fun createH2TextStyle() = TextStyle(
    fontSize = 28.dp.asSp(),
    lineHeight = 36.dp.asSp()
).normal()

@Composable
private fun createH3TextStyle() = TextStyle(
    fontSize = 24.dp.asSp(),
    lineHeight = 30.dp.asSp()
).normal()

@Composable
internal fun createH4TextStyle() = TextStyle(
    fontSize = 20.dp.asSp(),
    lineHeight = 26.dp.asSp()
).normal()

@Composable
internal fun createH5TextStyle() = TextStyle(
    fontSize = 18.dp.asSp(),
    lineHeight = 24.dp.asSp()
).normal()

@Composable
internal fun createBody1TextStyle() = TextStyle(
    fontSize = 16.dp.asSp(),
    lineHeight = 22.dp.asSp()
).normal()

@Composable
internal fun createBody2TextStyle() = TextStyle(
    fontSize = 14.dp.asSp(),
    lineHeight = 18.dp.asSp()
).normal()

@Composable
internal fun createCaption1TextStyle() = TextStyle(
    fontSize = 12.dp.asSp(),
    lineHeight = 16.dp.asSp()
).normal()

@Composable
internal fun createCaption2TextStyle() = TextStyle(
    fontSize = 11.dp.asSp(),
    lineHeight = 14.dp.asSp()
).normal()