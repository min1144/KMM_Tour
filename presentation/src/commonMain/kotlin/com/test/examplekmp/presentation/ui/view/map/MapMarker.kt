package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import com.test.examplekmp.domain.entity.map.LatLng

expect class Marker

class MarkerState(
    position: LatLng = LatLng(0.0, 0.0)
) {
    var position: LatLng by mutableStateOf(position)

    fun updatePosition(latLng: LatLng) {
        position = latLng
    }

    // The marker associated with this MarkerState.
    private val markerState: MutableState<Marker?> = mutableStateOf(null)
    internal var marker: Marker?
        get() = markerState.value
        set(value) {
            if (markerState.value == null && value == null) return
            if (markerState.value != null && value != null) {
                error("MarkerState may only be associated with one Marker at a time.")
            }
            markerState.value = value
        }

    companion object {

        val Saver: Saver<MarkerState, LatLng> = Saver(
            save = { it.position },
            restore = { MarkerState(it) }
        )
    }
}

@Composable
fun rememberMarkerState(
    key: String? = null,
    position: LatLng = LatLng(0.0, 0.0)
): MarkerState = rememberSaveable(key = key, saver = MarkerState.Saver) {
    MarkerState(position)
}

@Composable
expect fun MapMarker(
    state: MarkerState = rememberMarkerState(),
    alpha: Float = 1.0f,
    anchor: Offset = Offset(1.5f, 1.0f),
    flat: Boolean = false,
    rotation: Float = 0.0f,
    snippet: String? = null,
    tag: Any? = null,
    title: String? = null,
    visible: Boolean = true,
    zIndex: Float = 0.0f,
    onClick: (Marker) -> Boolean = { false }
)