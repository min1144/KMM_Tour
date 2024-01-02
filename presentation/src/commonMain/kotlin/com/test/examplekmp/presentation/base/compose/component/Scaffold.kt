package com.test.examplekmp.presentation.base.compose.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import com.test.examplekmp.presentation.base.root.WindowInsetStatusBarColor
import com.test.examplekmp.presentation.util.log.Logger.ui

private enum class ScaffoldLayoutContent { TopBar, MainContent }

interface DialogContent

object None : DialogContent

@Stable
class ScaffoldState(
    val dialogContent: MutableState<DialogContent> = mutableStateOf(None)
) {

    private var _overrideErrorDialogContent = mutableStateOf(false)

    val overrideErrorDialogContent
        get() = _overrideErrorDialogContent.value

    fun overrideErrorDialogContent(value: Boolean) {
        _overrideErrorDialogContent.value = value
    }

    fun closeDialog() {
        dialogContent.value = None
    }

    fun showDialog(value: DialogContent) {
        ui.d("showDialog >> $value")
        dialogContent.value = value
    }
}

@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    topBar: @Composable () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.background,
    statusBarColor: Color = MaterialTheme.colors.background,
    contentColor: Color = contentColorFor(backgroundColor),
    dialogContent: @Composable (DialogContent) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {

    WindowInsetStatusBarColor(statusBarColor)

    val child = @Composable { childModifier: Modifier ->
        Surface(modifier = childModifier, color = backgroundColor, contentColor = contentColor) {
            ScaffoldLayout(
                topBar = topBar,
                content = content
            )

            if (scaffoldState.dialogContent.value !is None) {
                dialogContent(scaffoldState.dialogContent.value)
            }
        }
    }

    child(modifier)
}


@Composable
private fun ScaffoldLayout(
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(layoutWidth, layoutHeight) {
            val topBarPlaceables = subcompose(ScaffoldLayoutContent.TopBar, topBar).takeIf {
                it.isNotEmpty()
            }?.fastMap {
                it.measure(looseConstraints)
            }

            val topBarHeight = topBarPlaceables?.maxBy { it.height }?.height ?: 0
            val bodyContentHeight = layoutHeight - topBarHeight

            val bodyContentPlaceables = subcompose(ScaffoldLayoutContent.MainContent) {
                val innerPadding = PaddingValues()
                content(innerPadding)
            }.fastMap { it.measure(looseConstraints.copy(maxHeight = bodyContentHeight)) }

            bodyContentPlaceables.fastForEach {
                it.place(0, topBarHeight)
            }
            topBarPlaceables?.fastForEach {
                it.place(0, 0)
            }
        }
    }
}

@Composable
fun rememberScaffoldState(
): ScaffoldState = remember {
    ScaffoldState()
}