package com.test.examplekmp.presentation.base.compose.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.sp
import com.test.examplekmp.presentation.RR
import com.test.examplekmp.presentation.base.compose.theme.AppTheme
import com.test.examplekmp.presentation.base.compose.theme.Colors
import com.test.examplekmp.presentation.base.compose.theme.semiBold
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun UnderlineText(
    title: String,
    color: Color = Colors.Black,
    modifier: Modifier
) {
    var selectedPartRects by remember { mutableStateOf(listOf<Rect>()) }

    Text(
        text = title,
        style = AppTheme.typography.h4.semiBold(color = color).copy(fontFamily = fontFamilyResource(
            RR.fonts.soyo_bold.soyo_bold)
        ),
        modifier = modifier.drawBehind {
            val borderSize = 20.sp.toPx()
            selectedPartRects.forEach { rect ->
                val selectedRect = rect.translate(0f, -borderSize / 1.5f)
                drawLine(
                    color = Color(0x408559DA),
                    start = Offset(selectedRect.left, selectedRect.bottom),
                    end = selectedRect.bottomRight,
                    strokeWidth = borderSize
                )
            }
        },
        onTextLayout = { layoutResult ->
            val start = 0
            val end = title.length
            selectedPartRects = if (start < end) {
                layoutResult.getBoundingBoxesForRange(start = start, end = end - 1)
            } else {
                emptyList()
            }
        }
    )
}

fun TextLayoutResult.getBoundingBoxesForRange(start: Int, end: Int): List<Rect> {
    var prevRect: Rect? = null
    var firstLineCharRect: Rect? = null
    val boundingBoxes = mutableListOf<Rect>()
    for (i in start..end) {
        val rect = getBoundingBox(i)
        val isLastRect = i == end

        // single char case
        if (isLastRect && firstLineCharRect == null) {
            firstLineCharRect = rect
            prevRect = rect
        }

        // `rect.right` is zero for the last space in each line
        // looks like an issue to me, reported: https://issuetracker.google.com/issues/197146630
        if (!isLastRect && rect.right == 0f) continue

        if (firstLineCharRect == null) {
            firstLineCharRect = rect
        } else if (prevRect != null) {
            if (prevRect.bottom != rect.bottom || isLastRect) {
                boundingBoxes.add(
                    firstLineCharRect.copy(right = prevRect.right)
                )
                firstLineCharRect = rect
            }
        }
        prevRect = rect

        if(isLastRect) {
            boundingBoxes.add(
                firstLineCharRect.copy(right = prevRect.right)
            )
        }
    }
    return boundingBoxes
}