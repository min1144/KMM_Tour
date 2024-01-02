package com.test.examplekmp.presentation.util.resource

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.mp.KoinPlatformTools

actual fun Int.pxToDp(): Dp {
    val context = KoinPlatformTools.defaultContext().get().get<Context>()
    val density = context.resources.displayMetrics.density
    return (this.toFloat() / density).dp
}

actual fun Dp.dpToPx(): Int {
    val context = KoinPlatformTools.defaultContext().get().get<Context>()
    val density = context.resources.displayMetrics.density
    val fontScale = context.resources.configuration.fontScale

    return (this.value * fontScale * density).toInt()
}