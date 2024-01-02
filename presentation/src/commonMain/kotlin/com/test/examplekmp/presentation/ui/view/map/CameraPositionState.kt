package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import com.test.examplekmp.domain.entity.map.LatLng

enum class CameraUpdateAnimation {
    Fly,
    EaseOut,
    EaseIn,
    Linear,
    None
}

expect class CameraUpdateParam

@Composable
inline fun rememberCameraPositionState(
    key: String? = null,
    crossinline init: CameraPositionState.() -> Unit = {}
): CameraPositionState = rememberSaveable(key = key, saver = CameraPositionState.Saver) {
    CameraPositionState().apply(init)
}

expect class CameraPositionState(
    position: CameraPosition = CameraPosition(
        LatLng(DEFAULT_LAT, DEFAULT_LON),
        zoom = DEFAULT_ZOOM_LEVEL,
        tilt = 5f,
        bearing = 0f
    )
) {

    var position: CameraPosition

    var isMoving: Boolean
        internal set

    fun move(cameraUpdate: CameraUpdateParam)

    fun moveAnimation(cameraUpdate: CameraUpdateParam, animation: CameraUpdateAnimation)

    fun newLatLng(latLng: LatLng): CameraUpdateParam

    fun scrollBy(xPixel: Float, yPixel: Float): CameraUpdateParam

    fun zoomIn(): CameraUpdateParam

    fun zoomOut(): CameraUpdateParam

    companion object {

        val Saver: Saver<CameraPositionState, CameraPosition>
    }
}

internal val LocalCameraPositionState = staticCompositionLocalOf { CameraPositionState() }

