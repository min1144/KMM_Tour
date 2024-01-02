package com.test.examplekmp.presentation.base.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun Icon(painter: ImageResource, tint: Color? = null, modifier: Modifier = Modifier) {
    val paint = painterResource(painter)
    if (tint != null) {
        androidx.compose.material.Icon(
            painter = paint,
            contentDescription = "",
            tint = tint,
            modifier = modifier
        )
    } else {
        androidx.compose.material.Icon(
            painter = paint,
            contentDescription = "",
            modifier = modifier
        )
    }
}

@Composable
fun Image(painter: Painter, modifier: Modifier = Modifier) {
    androidx.compose.foundation.Image(
        painter = painter,
        contentDescription = "",
        modifier = modifier
    )
}

@Composable
fun rememberThumbnailUrl(url: String): String = remember { url }