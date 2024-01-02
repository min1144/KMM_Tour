package com.test.examplekmp.presentation.util.resource

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import platform.UIKit.UIScreen


actual fun Int.pxToDp(): Dp = (this / UIScreen.mainScreen.scale).dp

actual fun Dp.dpToPx(): Int = (this.value * UIScreen.mainScreen.scale).toInt()