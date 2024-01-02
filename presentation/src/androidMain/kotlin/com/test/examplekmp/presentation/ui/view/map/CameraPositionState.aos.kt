package com.test.examplekmp.presentation.ui.view.map

import android.graphics.PointF
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.CameraUpdateParams
import com.naver.maps.map.NaverMap
import com.test.examplekmp.domain.entity.map.LatLng
import kotlinx.atomicfu.atomic

actual typealias CameraUpdateParam = CameraUpdateParams

actual class CameraPositionState actual constructor(position: CameraPosition) {

    internal var rawPosition by mutableStateOf(position.toRawCameraPosition())

    var map: NaverMap? by mutableStateOf(null)
        private set

    internal fun setMap(map: NaverMap?) {
        if (lock.compareAndSet(expect = false, update = true)) {
            if (this.map == null && map == null) return
            if (this.map != null && map != null) {
                error("CameraPositionState may only be associated with one GoogleMap at a time")
            }
            this.map = map
            if (map == null) {
                isMoving = false
            } else {
                map.moveCamera(CameraUpdate.toCameraPosition(position.toRawCameraPosition()))
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
                    map.moveCamera(CameraUpdate.toCameraPosition(value.toRawCameraPosition()))
                }
                lock.value = false
            }
        }

    actual var isMoving: Boolean by mutableStateOf(false)

    actual fun move(cameraUpdate: CameraUpdateParam) {
        map?.moveCamera(CameraUpdate.withParams(cameraUpdate))
    }

    actual fun moveAnimation(
        cameraUpdate: CameraUpdateParam,
        animation: CameraUpdateAnimation
    ) {
        val param = CameraUpdate.withParams(cameraUpdate)
        val cameraAnimation = when(animation){
            CameraUpdateAnimation.Fly -> CameraAnimation.Fly
            CameraUpdateAnimation.EaseOut -> CameraAnimation.Easing
            CameraUpdateAnimation.EaseIn -> CameraAnimation.Easing
            CameraUpdateAnimation.Linear -> CameraAnimation.Linear
            CameraUpdateAnimation.None -> CameraAnimation.None
        }
        map?.moveCamera(param.animate(cameraAnimation))
    }

    actual fun newLatLng(latLng: LatLng): CameraUpdateParam =
        CameraUpdateParams().scrollTo(com.naver.maps.geometry.LatLng(latLng.latitude, latLng.longitude))

    actual fun scrollBy(xPixel: Float, yPixel: Float): CameraUpdateParam =
        CameraUpdateParams().scrollBy(PointF(xPixel, yPixel))

    actual fun zoomIn(): CameraUpdateParam =
        CameraUpdateParams().zoomIn()

    actual fun zoomOut(): CameraUpdateParam =
        CameraUpdateParams().zoomOut()

    actual companion object {

        actual val Saver: Saver<CameraPositionState, CameraPosition>
            get() = Saver(
                save = { it.position },
                restore = { CameraPositionState().apply { position = it } }
            )
    }
}

internal fun CameraPosition.toRawCameraPosition() = com.naver.maps.map.CameraPosition(
    com.naver.maps.geometry.LatLng(target.latitude, target.longitude),
    zoom.toDouble(),
    tilt.toDouble(),
    bearing.toDouble()
)

internal fun com.naver.maps.map.CameraPosition.toCameraPosition() = CameraPosition(
    LatLng(target.latitude, target.longitude),
    zoom.toFloat(),
    tilt.toFloat(),
    bearing.toFloat()
)