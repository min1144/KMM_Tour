package com.test.examplekmp.presentation.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.test.examplekmp.domain.entity.map.LatLng
import com.test.examplekmp.presentation.base.compose.component.ScaffoldState
import com.test.examplekmp.presentation.base.compose.component.rememberScaffoldState
import com.test.examplekmp.presentation.model.Location
import com.test.examplekmp.presentation.ui.view.map.CameraPosition
import com.test.examplekmp.presentation.ui.view.map.CameraPositionState
import com.test.examplekmp.presentation.ui.view.map.CurrentLocationState
import com.test.examplekmp.presentation.ui.view.map.DEFAULT_LAT
import com.test.examplekmp.presentation.ui.view.map.DEFAULT_LON
import com.test.examplekmp.presentation.ui.view.map.MapControlState
import com.test.examplekmp.presentation.ui.view.map.rememberCameraPositionState
import com.test.examplekmp.presentation.ui.view.map.rememberCurrentLocationState
import com.test.examplekmp.presentation.ui.view.map.rememberMapControlState

class WorkMapScreenState(
    val scaffoldState: ScaffoldState,
    val cameraPositionState: CameraPositionState,
    val mapControlState: MapControlState,
    val currentLocationState: CurrentLocationState,
) {

    fun moveCameraPosition(latLng: Location) {
        currentLocationState.updateGPSCurrentLocation(latLng)
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            latLng.toLatLngInfo(), mapControlState.currentZoomLevel
        )
    }
}

@Composable
internal fun rememberWorkMapScreenState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    mapControlState: MapControlState = rememberMapControlState(),
    cameraPositionState: CameraPositionState = rememberCameraPositionState(key = ""){
        position = position.copy(
            target = LatLng(DEFAULT_LAT, DEFAULT_LON),
            zoom = mapControlState.currentZoomLevel
        )
    },
    currentLocationState: CurrentLocationState = rememberCurrentLocationState(latLng = LatLng(DEFAULT_LAT, DEFAULT_LON))
) = remember(scaffoldState, cameraPositionState) {
    WorkMapScreenState(
        scaffoldState = scaffoldState,
        cameraPositionState = cameraPositionState,
        mapControlState = mapControlState,
        currentLocationState = currentLocationState
    )
}

fun Location.toLatLngInfo(): LatLng {
    return LatLng(latitude, longitude)
}