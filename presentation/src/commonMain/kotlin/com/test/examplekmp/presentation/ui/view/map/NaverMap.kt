package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.test.examplekmp.domain.entity.map.LatLng

@Composable
expect fun NaverMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit = {},
    onMapClick: (lat: LatLng) -> Unit = {},
    mapContent: @Composable () -> Unit = {},
    controllerContent: @Composable () -> Unit = {},
    overlayContent: @Composable () -> Unit = {}
)