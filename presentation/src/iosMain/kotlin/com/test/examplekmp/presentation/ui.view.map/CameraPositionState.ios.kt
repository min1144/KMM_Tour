@file:OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)

package com.test.examplekmp.presentation.ui.view.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import cocoapods.NMapsMap.NMFCameraPosition
import cocoapods.NMapsMap.NMFCameraUpdate
import cocoapods.NMapsMap.NMFCameraUpdateAnimation
import cocoapods.NMapsMap.NMFCameraUpdateParams
import cocoapods.NMapsMap.NMFMapView
import cocoapods.NMapsMap.NMGLatLng
import com.test.examplekmp.domain.entity.map.LatLng
import kotlinx.atomicfu.atomic
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGPointMake

actual typealias CameraUpdateParam = NMFCameraUpdateParams

actual class CameraPositionState actual constructor(position: CameraPosition) {

    internal var rawPosition by mutableStateOf(position.toRawCameraPosition())

    var map: NMFMapView? by mutableStateOf(null)
        private set

    internal fun setMap(map: NMFMapView?) {
        if (lock.compareAndSet(expect = false, update = true)) {
            if (this.map == null && map == null) return
            if (this.map != null && map != null) {
                error("CameraPositionState may only be associated with one GoogleMap at a time")
            }
            this.map = map
            if (map == null) {
                isMoving = false
            } else {
                map.moveCamera(NMFCameraUpdate.cameraUpdateWithPosition(position.toRawCameraPosition()))
            }

            lock.value = false
        }
    }

    // Used to perform side effects thread-safely.
    // Guards all mutable properties that are not `by mutableStateOf`.
    private val lock = atomic(false)

    actual var position: CameraPosition
        get() = rawPosition.toCameraPosition()
        set(value) {
            if (lock.compareAndSet(expect = false, update = true)) {
                val map = map
                if (map == null) {
                    rawPosition = value.toRawCameraPosition()
                } else {
                    map.moveCamera(NMFCameraUpdate.cameraUpdateWithPosition(value.toRawCameraPosition()))
                }
                lock.value = false
            }
        }

    actual var isMoving: Boolean by mutableStateOf(false)

    actual fun move(cameraUpdate: CameraUpdateParam) {
        map?.moveCamera(NMFCameraUpdate.cameraUpdateWithParams(cameraUpdate))
    }

    actual fun moveAnimation(
        cameraUpdate: CameraUpdateParam,
        animation: CameraUpdateAnimation
    ) {
        val param = NMFCameraUpdate.cameraUpdateWithParams(cameraUpdate)
        val nmfCameraAnimation = when(animation){
            CameraUpdateAnimation.Fly -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationFly
            CameraUpdateAnimation.EaseOut -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationEaseOut
            CameraUpdateAnimation.EaseIn -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationEaseIn
            CameraUpdateAnimation.Linear -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationLinear
            CameraUpdateAnimation.None -> NMFCameraUpdateAnimation.NMFCameraUpdateAnimationNone
        }
        param.setAnimation(nmfCameraAnimation)
        map?.moveCamera(param)
    }

    actual fun newLatLng(latLng: LatLng): CameraUpdateParam =
        NMFCameraUpdateParams().apply {
            scrollTo(NMGLatLng().apply {
                setLat(latLng.latitude)
                setLng(latLng.longitude)
            })
        }

    @OptIn(ExperimentalForeignApi::class)
    actual fun scrollBy(xPixel: Float, yPixel: Float): CameraUpdateParam =
        NMFCameraUpdateParams().apply {
            scrollBy(CGPointMake(xPixel.toDouble(), yPixel.toDouble()))
        }

    actual fun zoomIn(): CameraUpdateParam =
        NMFCameraUpdateParams().apply {
            zoomIn()
        }

    actual fun zoomOut(): CameraUpdateParam =
        NMFCameraUpdateParams().apply {
            zoomOut()
        }

    actual companion object {

        actual val Saver: Saver<CameraPositionState, CameraPosition>
            get() = Saver(
                save = { it.position },
                restore = { CameraPositionState().apply { position = it } }
            )
    }
}

internal fun CameraPosition.toRawCameraPosition() = NMFCameraPosition.cameraPosition(
    NMGLatLng().apply {
        setLat(target.latitude)
        setLng(target.longitude)
    },
    zoom.toDouble(),
    tilt.toDouble(),
    bearing.toDouble()
)

internal fun NMFCameraPosition.toCameraPosition() = CameraPosition(
    LatLng(target.lat(), target.lng()),
    zoom.toFloat(),
    tilt.toFloat(),
    heading.toFloat()
)