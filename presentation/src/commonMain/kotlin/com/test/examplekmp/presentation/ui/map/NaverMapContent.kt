package com.test.examplekmp.presentation.ui.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.test.examplekmp.presentation.base.compose.component.fastForEach
import com.test.examplekmp.presentation.model.Marker
import com.test.examplekmp.presentation.ui.view.PlaceDetail
import com.test.examplekmp.presentation.ui.view.map.CameraPositionState
import com.test.examplekmp.presentation.ui.view.map.MapControlState
import com.test.examplekmp.presentation.ui.view.map.MapControllerContent
import com.test.examplekmp.presentation.ui.view.map.NaverMap
import com.test.examplekmp.presentation.ui.view.map.PlaceMarker

@Composable
fun NaverMapContent(
    cameraPositionState: CameraPositionState,
    mapControlState: MapControlState,
    markerList: State<List<Marker>>,
    selectMarker: State<Marker?>,
    onMapLoaded: () -> Unit,
    onResetClickMarker: () -> Unit,
    onSelfRefreshMapClick: () -> Unit,
    onSelectClickMarker: (marker: Marker) -> Unit,
    onBuildingDetailsClick: (marker: Marker) -> Unit,
) {
    NaverMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLoaded = onMapLoaded,
        onMapClick = {
            onResetClickMarker()
        },
        mapContent = {
            val items = markerList.value
            items.fastForEach { marker ->
                PlaceMarker(marker, onClick = onSelectClickMarker)
            }
        },
        controllerContent = {
            MapControllerContent(
                mapControlState,
                cameraPositionState,
                onSelfRefreshMapClick = onSelfRefreshMapClick,
            )
        },
        overlayContent = {
            selectMarker.value?.let { marker ->
                PlaceDetail(marker,
                    onClick = { onBuildingDetailsClick(marker) }
                )
            }
        }
    )
}