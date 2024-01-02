package com.test.examplekmp.presentation.ui.view.map

import android.graphics.PointF
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.ui.geometry.Offset
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap

actual typealias Marker = com.naver.maps.map.overlay.Marker

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
            val marker =  com.naver.maps.map.overlay.Marker().apply {
                this.position = state.position.run { LatLng(latitude, longitude) }
                title?.let { this.captionText = title }
                snippet?.let { this.subCaptionText = snippet }
                this.isFlat = flat
                this.angle = rotation
                this.alpha = alpha
                this.zIndex = zIndex.toInt()
                this.anchor = PointF(anchor.x, anchor.y)
                this.setOnClickListener {
                    onClick.invoke(this)
                }
            }
            marker.tag = tag
            MarkerNode(
                mapView = cameraPositionState.map,
                marker = marker,
                markerState = state
            )
        },
        update = {
            update(onClick) {
                this.marker.setOnClickListener { _ ->
                    it.invoke(this.marker)
                }
            }

            set(alpha) { this.marker.alpha = it }
            set(anchor) { this.marker.anchor = PointF(anchor.x, anchor.y) }
            set(flat) { this.marker.isFlat = it }
            set(state.position) { this.marker.position = LatLng(it.latitude, it.longitude) }
            set(rotation) { this.marker.angle = it }
            set(snippet) {
                it?.let { this.marker.subCaptionText = it }
            }
            set(tag) { this.marker.tag = it }
            set(title) {
                it?.let { this.marker.captionText = it }
            }
            set(zIndex) { this.marker.zIndex = it.toInt() }
        }
    )
}

internal class MarkerNode(
    private val mapView: NaverMap?,
    val marker: com.naver.maps.map.overlay.Marker,
    val markerState: MarkerState
) : MapNode {
    override fun onAttached() {
        markerState.marker = marker
        marker.map = mapView
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

private fun com.naver.maps.map.overlay.Marker.remove() {
    map = null
}