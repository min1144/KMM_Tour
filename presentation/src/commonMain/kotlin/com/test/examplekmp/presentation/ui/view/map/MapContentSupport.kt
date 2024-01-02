package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapControllerContent(
    mapControlState: MapControlState,
    cameraPositionState: CameraPositionState,
    onSelfRefreshMapClick: () -> Unit,
) {
    MapControl(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, end = 10.dp),
        state = mapControlState,
        cameraPositionState = cameraPositionState,
        onUpdateMap = {
            onSelfRefreshMapClick()
        },
    )
}