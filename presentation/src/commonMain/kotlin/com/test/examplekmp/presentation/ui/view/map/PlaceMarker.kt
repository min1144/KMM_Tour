package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.runtime.Composable
import com.test.examplekmp.presentation.model.Marker

@Composable
fun PlaceMarker(
    marker: Marker,
    onClick: (marker: Marker) -> Unit
) {
    val state = rememberMarkerState(marker)
    val latLngFromMarker = marker.latLng
    state.updatePosition(latLngFromMarker)

    val cameraPositionState = LocalCameraPositionState.current
    MapMarker(
        state = state,
        zIndex = if (marker.select) 1.0f else 0f,
        onClick = {
            cameraPositionState.moveAnimation(
                cameraPositionState.newLatLng(latLngFromMarker),
                CameraUpdateAnimation.Linear
            )
            onClick(marker)
            true
        },
        tag = marker.keyArray()
    )
}

@Composable
private fun rememberMarkerState(marker: Marker): MarkerState {
    val markerKey = marker.contentId
    val state = with(marker) {
        rememberMarkerState(
            key = markerKey, position = latLng
        )
    }
    return state
}

private fun Marker.keyArray(): Array<Any?> {
    if(this is Marker.PlaceMarker) {
        return arrayOf(
            name,
            address,
            lat,
            lng,
            contentId,
            contentTypeId,
            select,
            thumbnailUrl)
    }
    return emptyArray()
}