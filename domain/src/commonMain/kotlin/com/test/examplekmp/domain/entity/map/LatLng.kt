package com.test.examplekmp.domain.entity.map

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class LatLng(
    val latitude: Double,
    val longitude: Double
): Parcelable