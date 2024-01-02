@file:OptIn(ExperimentalForeignApi::class)

package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import cocoapods.NMapsMap.NMFCameraPosition
import cocoapods.NMapsMap.NMFCameraUpdate
import cocoapods.NMapsMap.NMFMapView
import cocoapods.NMapsMap.NMFMapViewCameraDelegateProtocol
import cocoapods.NMapsMap.NMFMapViewTouchDelegateProtocol
import cocoapods.NMapsMap.NMGLatLng
import com.test.examplekmp.domain.entity.map.LatLng
import com.test.examplekmp.presentation.util.koin.koinContext
import com.test.examplekmp.presentation.util.log.Logger
import com.test.examplekmp.presentation.util.swift.delegate.SwiftDelegateFactory
import com.test.examplekmp.presentation.util.swift.delegate.SwiftNaverMapDelegate
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.awaitCancellation
import platform.CoreGraphics.CGPoint
import platform.darwin.NSObject

@Composable
actual fun NaverMap(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    onMapClick: (lat: LatLng) -> Unit,
    mapContent: @Composable () -> Unit,
    controllerContent: @Composable () -> Unit,
    overlayContent: @Composable () -> Unit
) {

    Box(modifier) {
        NaverMapInternal(
            modifier = modifier.matchParentSize(),
            cameraPositionState,
            onMapLoaded,
            onMapClick
        ) {
            mapContent()
        }
        controllerContent()
        overlayContent()
    }
}


@OptIn(ExperimentalForeignApi::class)
@Composable
private fun NaverMapInternal(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    onMapClick: (lat: LatLng) -> Unit,
    content: @Composable () -> Unit
) {
    val mapView = remember { NMFMapView() }
    val currentCameraPositionState by rememberUpdatedState(cameraPositionState)
    val currentContent by rememberUpdatedState(content)
    val parentComposition = rememberCompositionContext()
    val mapApplier = remember { MapApplier(mapView) }
    val mapClickListeners = remember { MapClickListeners() }.also {
        it.onMapClick = onMapClick
        it.onMapLoaded = onMapLoaded
    }

    UIKitView(
        modifier = modifier,
        factory = { mapView }
    )

    LaunchedEffect(Unit) {
        disposingComposition {
            mapView.newComposition(mapApplier, parentComposition) {
                MapUpdater(
                    cameraPositionState = currentCameraPositionState,
                    clickListeners = mapClickListeners
                )
                CompositionLocalProvider(
                    LocalCameraPositionState provides cameraPositionState
                ) {
                    currentContent.invoke()
                }
            }
        }
    }
}

internal suspend inline fun disposingComposition(factory: () -> Composition) {
    val composition = factory()
    try {
        awaitCancellation()
    } finally {
        composition.dispose()
    }
}

private suspend inline fun NMFMapView.newComposition(
    mapApplier: MapApplier,
    parent: CompositionContext,
    noinline content: @Composable () -> Unit
): Composition {
    return Composition(
        mapApplier, parent
    ).apply {
        setContent(content)
    }
}

internal class MapPropertiesNode(
    private val mapView: NMFMapView,
    cameraPositionState: CameraPositionState,
    var clickListeners: MapClickListeners,
    var layoutDirection: LayoutDirection
) : MapNode {

    private var cameraDelegateProtocol: NMFMapViewCameraDelegateProtocol? = null

    private var touchDelegateProtocol: NMFMapViewTouchDelegateProtocol? = null

    init {
        cameraPositionState.apply {
            setMap(mapView)
            mapView.moveCamera(
                NMFCameraUpdate.cameraUpdateWithPosition(
                    NMFCameraPosition.cameraPosition(
                position.target.run {
                    NMGLatLng().apply {
                        setLat(latitude)
                        setLng(longitude)
                    }
                },
                position.zoom.toDouble(),
                position.tilt.toDouble(),
                position.bearing.toDouble()
            )))
        }
    }

    var cameraPositionState = cameraPositionState
        set(value) {
            if (value == field) return
            field.setMap(null)
            field = value
            value.setMap(mapView)
        }

    @ExperimentalForeignApi
    override fun onAttached() {
        cameraDelegateProtocol = koinContext<SwiftDelegateFactory>().naverMapDelegate(object : SwiftNaverMapDelegate {
            override fun setOnCameraIdleListener(position: NMFCameraPosition) {
                with(position.target) {
                    Logger.default.d("camera position Idle: ${lat()}, ${lng()}")
                    cameraPositionState.isMoving = false
                    cameraPositionState.rawPosition = position
                }
            }

            override fun setOnCameraMoveCanceledListener(position: NMFCameraPosition) {
                cameraPositionState.isMoving = false
                with(position.target) {
                    Logger.default.d("camera position Cancel: ${lat()}, ${lng()}")
                }
            }

            override fun setOnCameraMoveStartedListener(reason: Int) {
                cameraPositionState.isMoving = true
                Logger.default.d("camera position Start: $reason")
            }

            override fun setOnCameraMoveListener(position: NMFCameraPosition) {
                with(position.target) {
                    Logger.default.d("camera position Move: ${lat()}, ${lng()}")
                }
                cameraPositionState.rawPosition = position
            }
        }).apply {
            mapView.addCameraDelegate(this)
        }
        touchDelegateProtocol = object : NSObject(), NMFMapViewTouchDelegateProtocol {

            override fun mapView(
                mapView: NMFMapView,
                didTapMap: NMGLatLng,
                point: CValue<CGPoint>
            ) {
                clickListeners.onMapClick(LatLng(didTapMap.lat(), didTapMap.lng()))
            }
        }.apply {
            mapView.setTouchDelegate(this)
        }
        clickListeners.onMapLoaded()
        Logger.default.d("MapView delegate : ${mapView.touchDelegate}")
    }

    override fun onRemoved() {
        cameraPositionState.setMap(null)
    }

    override fun onCleared() {
        cameraPositionState.setMap(null)
    }
}

@Composable
internal inline fun MapUpdater(
    cameraPositionState: CameraPositionState,
    clickListeners: MapClickListeners
) {
    val applier = currentComposer.applier as MapApplier
    val map = applier.mapView
    val layoutDirection = LocalLayoutDirection.current
    ComposeNode<MapPropertiesNode, MapApplier>(
        factory = {
            MapPropertiesNode(
                mapView = map,
                cameraPositionState = cameraPositionState,
                clickListeners = clickListeners,
                layoutDirection = layoutDirection,
            )
        }
    ) {
        update(layoutDirection) { this.layoutDirection = it }

        update(cameraPositionState) { this.cameraPositionState = it }
        update(clickListeners) { this.clickListeners = it }
    }
}

internal class MapClickListeners {
    var onMapClick: (LatLng) -> Unit by mutableStateOf({})
    var onMapLoaded: () -> Unit by mutableStateOf({})
    var onMyLocationButtonClick: () -> Boolean by mutableStateOf({ false })
}