package com.test.examplekmp.presentation.base.compose.component

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.test.examplekmp.presentation.base.compose.theme.Colors

@Composable
fun HyperlinkText(prefixText: String = "", hyperlinkText: String, endText: String = "",
                  url: String
) {

    val annotatedString = buildAnnotatedString {
        append(prefixText)
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Colors.Blue)) {
            append(hyperlinkText)
            addStringAnnotation(
                tag = "URL",
                annotation = url,
                start = length - hyperlinkText.length,
                end = length
            )
        }
        append(endText)
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        }
    )
}
