package com.test.examplekmp.presentation.base.compose.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

fun TextStyle.normal(color: Color = Colors.Black): TextStyle {
    return this.copy(fontWeight = FontWeight.Normal, color = color)
}

fun TextStyle.semiBold(color: Color = Colors.Black): TextStyle {
    return this.copy(fontWeight = FontWeight.SemiBold, color = color)
}

fun TextStyle.bold(color: Color = Colors.Black): TextStyle {
    return this.copy(fontWeight = FontWeight.Bold, color = color)
}