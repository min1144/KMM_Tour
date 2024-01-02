@file:OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)

package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.ui.geometry.Offset
import cocoapods.NMapsMap.NMFMapView
import cocoapods.NMapsMap.NMFMarker
import cocoapods.NMapsMap.NMGLatLng
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGPointMake

actual typealias Marker = NMFMarker

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapMarker(
    state: MarkerState,
    alpha: Float,
    anchor: Offset,
    flat: Boolean,
    rotation: Float,
    snippet: String?,
    tag: Any?,
    title: String?,
    visible: Boolean,
    zIndex: Float,
    onClick: (Marker) -> Boolean
) {

    val cameraPositionState = LocalCameraPositionState.current

    ComposeNode<MarkerNode, MapApplier>(
        factory = {
            val marker =  NMFMarker().apply {
                this.position = state.position.run {
                    NMGLatLng().apply {
                        setLat(latitude)
                        setLng(longitude)
                    }
                }
                title?.let { this.captionText = title }
                snippet?.let { this.subCaptionText = snippet }
                this.setFlat(flat)
                this.angle = rotation.toDouble()
                this.alpha = alpha.toDouble()
                this.zIndex = zIndex.toLong()
                this.anchor = CGPointMake(anchor.x.toDouble(), anchor.y.toDouble())
                this.touchHandler = {
                    onClick.invoke(this)
                    true
                }
            }
            MarkerNode(
                mapView = cameraPositionState.map,
                marker = marker,
                markerState = state,
                onMarkerClick = onClick
            )
        },
        update = {
            update(onClick) { this.onMarkerClick = it }

            set(alpha) { this.marker.setAlpha(it.toDouble()) }
            set(anchor) { this.marker.setAnchor(CGPointMake(anchor.x.toDouble(), anchor.y.toDouble())) }
            set(flat) { this.marker.setFlat(it) }
            set(state.position) { this.marker.position = NMGLatLng().apply {
                setLat(it.latitude)
                setLng(it.longitude)
            }
            }
            set(rotation) { this.marker.angle = it.toDouble() }
            set(snippet) {
                it?.let { this.marker.subCaptionText = it }
            }
            set(title) {
                it?.let { this.marker.captionText = it }
            }
            set(zIndex) { this.marker.zIndex = it.toLong() }
            set(onClick) { this.marker.touchHandler = { _ ->
                it.invoke(this.marker)
                true
            }}
        }
    )
}

internal class MarkerNode(
    private val mapView: NMFMapView?,
    val marker: NMFMarker,
    val markerState: MarkerState,
    var onMarkerClick: (Marker) -> Boolean,
) : MapNode {
    override fun onAttached() {
        markerState.marker = marker
        marker.setMapView(mapView)
    }
    override fun onRemoved() {
        markerState.marker = null
        marker.remove()
    }

    override fun onCleared() {
        markerState.marker = null
        marker.remove()
    }
}

private fun NMFMarker.remove() {
    setMapView(null)
}