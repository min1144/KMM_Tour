package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.test.examplekmp.presentation.RR
import com.test.examplekmp.presentation.base.compose.Icon
import com.test.examplekmp.presentation.base.compose.theme.Colors
import dev.icerock.moko.resources.ImageResource
import kotlinx.coroutines.launch

private val defaultButtonSize = 42.dp

const val DEFAULT_ZOOM_LEVEL = 15f
const val DEFAULT_LAT = 37.5666805
const val DEFAULT_LON = 126.9784147
const val DEFAULT_RADIUS = 1000

@Stable
class MapControlState(
    zoomLevel: Float
) {

    var showUpdateMap = mutableStateOf(true)

    private var _zoomLevel = mutableFloatStateOf(zoomLevel)

    val currentZoomLevel: Float
        get() = _zoomLevel.floatValue

    fun setZoomLevel(level: Float) {
        _zoomLevel.floatValue = level
    }

    companion object {
        val Saver: Saver<MapControlState, *> = listSaver(save = {
            listOf(it._zoomLevel.floatValue)
        }, restore = { r ->
            val zoomLevel: Float = r[0] as Float
            MapControlState(zoomLevel)
        })
    }
}

@Composable
fun rememberMapControlState(
    zoomLevel: Float = DEFAULT_ZOOM_LEVEL
): MapControlState {
    return rememberSaveable(saver = MapControlState.Saver) {
        MapControlState(zoomLevel)
    }
}

@Composable
fun MapControl(
    modifier: Modifier = Modifier,
    state: MapControlState,
    cameraPositionState: CameraPositionState,
    onUpdateMap: () -> Unit = {

    },
    onZoomOut: () -> Unit = {

    },
    onZoomIn: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    MapControlInternal(
        modifier,
        state,
        onUpdateMap,
        onZoomOut = {
            scope.launch {
                val zoom = cameraPositionState.zoomOut()
                cameraPositionState.moveAnimation(zoom, CameraUpdateAnimation.EaseOut)
                state.setZoomLevel(cameraPositionState.position.zoom)
                onZoomOut()
            }
        },
        onZoomIn = {
            scope.launch {
                val zoom = cameraPositionState.zoomIn()
                cameraPositionState.moveAnimation(zoom, CameraUpdateAnimation.EaseIn)
                state.setZoomLevel(cameraPositionState.position.zoom)
                onZoomIn()
            }
        }
    )
}

@Composable
private fun MapControlInternal(
    modifier: Modifier,
    state: MapControlState,
    onUpdateMap: () -> Unit,
    onZoomOut: () -> Unit,
    onZoomIn: () -> Unit
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.End
    ) {
        val defaultSpacerHeight = 12.dp - defaultElevationDp()

        if (state.showUpdateMap.value) {
            IconButton(RR.images.ic_refresh, onClick = onUpdateMap)
            Spacer(modifier = Modifier.height(defaultSpacerHeight))
        }
        ZoomButton(onZoomOut = onZoomOut, onZoomIn = onZoomIn)
    }
}

@Composable
private fun ZoomButton(
    onZoomOut: () -> Unit, onZoomIn: () -> Unit
) {
    val elevationValue = defaultElevationDp()
    Surface(
        modifier = Modifier.width(defaultButtonSize),
        elevation = elevationValue
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(defaultButtonSize, defaultButtonSize)
                    .clickable(onClick = onZoomIn),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = RR.images.ic_plus)
            }
            Divider(color = Colors.Gray_200, thickness = 1.dp)
            Box(
                modifier = Modifier
                    .size(defaultButtonSize, defaultButtonSize)
                    .clickable(onClick = onZoomOut),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = RR.images.ic_minus)
            }
        }
    }
}

@Composable
private fun IconButton(icon: ImageResource, onClick: () -> Unit) {
    Surface (
        modifier = Modifier
            .size(defaultButtonSize, defaultButtonSize)
            .clickable(onClick = onClick),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(painter = icon)
        }
    }
}


@Composable
private fun defaultElevationDp(interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }): Dp {
    val elevation: ButtonElevation = ButtonDefaults.elevation()
    return elevation.elevation(true, interactionSource).value
}