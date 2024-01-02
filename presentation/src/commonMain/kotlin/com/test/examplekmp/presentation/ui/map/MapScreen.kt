package com.test.examplekmp.presentation.ui.map

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.examplekmp.presentation.base.bindViewModel
import com.test.examplekmp.presentation.base.compose.component.rememberScaffoldState
import com.test.examplekmp.presentation.base.compose.theme.Colors
import com.test.examplekmp.presentation.ui.DefaultScreen

@Composable
fun MapScreen(
    component: MapComponent,
    viewModel: MapViewModel = bindViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val state = rememberWorkMapScreenState()

    DefaultScreen(
        modifier = Modifier.padding(bottom = 80.dp),
        backgroundColor = Colors.White,
        scaffoldState = scaffoldState,
        loading = viewModel.loading,
    ) {
        val markerList = viewModel.markerList.collectAsState()
        val selectMarker = viewModel.selectMarker.collectAsState()

        NaverMapContent(
            state.cameraPositionState,
            state.mapControlState,
            markerList,
            selectMarker,
            onMapLoaded = {},
            onResetClickMarker = { },
            onSelfRefreshMapClick = {
                val target = state.cameraPositionState.position.target
                viewModel.getCurrentMarkerList(target)
            },
            onSelectClickMarker = {
                viewModel.selectMarker(it)
            },
            onBuildingDetailsClick = {
                component.onItemClick(contentId = it.contentId,
                    contentTypeId = it.contentTypeId
                )
            },
        )
    }
}