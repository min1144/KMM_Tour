package com.test.examplekmp.presentation.ui.view.map

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.test.examplekmp.domain.entity.map.LatLng

@Parcelize
data class CameraPosition(
    val target: LatLng,
    val zoom: Float,
    val tilt: Float = 0f,
    val bearing: Float = 0f
) : Parcelable {

    companion object {
        fun fromLatLngZoom(latLng: LatLng, zoom: Float) = CameraPosition(latLng, zoom)
    }
}
