package com.test.examplekmp.presentation.ui.view.map

import android.annotation.SuppressLint
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.test.examplekmp.domain.entity.map.LatLng
import com.test.examplekmp.presentation.base.root.LocalComponentContext
import com.test.examplekmp.presentation.extension.LifeCycleEvent
import com.test.examplekmp.presentation.extension.LifecycleEventObserver
import kotlinx.coroutines.awaitCancellation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

@Composable
private fun NaverMapInternal(
    modifier: Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    onMapClick: (lat: LatLng) -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context, NaverMapOptions())
    }

    val currentCameraPositionState by rememberUpdatedState(cameraPositionState)
    val currentContent by rememberUpdatedState(content)
    val parentComposition = rememberCompositionContext()
    val mapClickListeners = remember { MapClickListeners() }.also {
        it.onMapClick = onMapClick
        it.onMapLoaded = onMapLoaded
    }

    AndroidView(
        modifier = modifier,
        factory = { mapView }
    )

    MapLifecycle(mapView)

    LaunchedEffect(Unit) {
        disposingComposition {
            mapView.newComposition(parentComposition) {
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

private suspend inline fun MapView.newComposition(
    parent: CompositionContext,
    noinline content: @Composable () -> Unit
): Composition {
    val map = awaitMap()
    return Composition(
        MapApplier(map), parent
    ).apply {
        setContent(content)
    }
}

private suspend inline fun MapView.awaitMap(): NaverMap {
    return suspendCoroutine { continuation ->
        getMapAsync {
            continuation.resume(it)
        }
    }
}

@SuppressLint("NewApi")
@Composable
private fun MapLifecycle(mapView: MapView) {
    val context = LocalContext.current
    val lifecycle = LocalComponentContext.current.lifecycle
    val previousState = remember { mutableStateOf(LifeCycleEvent.ON_CREATE) }
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(
            previousState,
        )
        val callbacks = mapView.componentCallbacks()

        lifecycle.subscribe(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            lifecycle.unsubscribe(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)

            when (previousState.value) {
                LifeCycleEvent.ON_CREATE, LifeCycleEvent.ON_STOP -> {
                    mapView.onDestroy()
                }
                LifeCycleEvent.ON_START, LifeCycleEvent.ON_PAUSE -> {
                    mapView.onStop()
                    mapView.onDestroy()
                }
                LifeCycleEvent.ON_RESUME -> {
                    mapView.onPause()
                    mapView.onStop()
                    mapView.onDestroy()
                }
                else -> {}
            }
        }
    }
}

private fun MapView.lifecycleObserver(
    previousState: MutableState<LifeCycleEvent>,
): Lifecycle.Callbacks {
    return LifecycleEventObserver { event ->
        when (event) {
            LifeCycleEvent.ON_CREATE -> this.onCreate(Bundle())
            LifeCycleEvent.ON_START -> this.onStart()
            LifeCycleEvent.ON_RESUME -> this.onResume()
            LifeCycleEvent.ON_PAUSE -> this.onPause()
            LifeCycleEvent.ON_STOP -> this.onStop()
            LifeCycleEvent.ON_DESTROY -> this.onDestroy()
            else -> throw IllegalStateException()
        }
        previousState.value = event
    }
}

private fun MapView.componentCallbacks(): ComponentCallbacks {
    return object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {}

        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
        }
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
                map = map,
                mapApplier = applier,
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

internal class MapPropertiesNode(
    private val map: NaverMap,
    private val mapApplier: MapApplier,
    cameraPositionState: CameraPositionState,
    var clickListeners: MapClickListeners,
    var layoutDirection: LayoutDirection
) : MapNode {

    init {
        cameraPositionState.apply {
            setMap(this@MapPropertiesNode.map)
            this@MapPropertiesNode.map.moveCamera(CameraUpdate.toCameraPosition(position.toRawCameraPosition()))
        }
    }

    var cameraPositionState = cameraPositionState
        set(value) {
            if (value == field) return
            field.setMap(null)
            field = value
            value.setMap(map)
        }

    override fun onAttached() {
        map.uiSettings.isZoomControlEnabled = false
        map.uiSettings.isLogoClickEnabled = false
        map.setOnMapClickListener { _, latLng ->
            clickListeners.onMapClick(LatLng(latLng.latitude, latLng.longitude))
        }

        map.addOnCameraIdleListener {
            cameraPositionState.isMoving = false
            cameraPositionState.rawPosition = map.cameraPosition
        }

        map.addOnCameraChangeListener { reason, animated ->
            if (!cameraPositionState.isMoving) {
                cameraPositionState.isMoving = true
            }
            cameraPositionState.rawPosition = map.cameraPosition
        }

        map.addOnLoadListener {
            clickListeners.onMapLoaded()
        }
    }

    override fun onRemoved() {
        cameraPositionState.setMap(null)
    }

    override fun onCleared() {
        cameraPositionState.setMap(null)
    }
}

internal class MapClickListeners {
    var onMapClick: (LatLng) -> Unit by mutableStateOf({})
    var onMapLoaded: () -> Unit by mutableStateOf({})
}