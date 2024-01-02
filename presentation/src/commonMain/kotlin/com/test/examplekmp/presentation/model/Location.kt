package com.test.examplekmp.presentation.model

import com.arkivanov.essenty.parcelable.Parcelize
import dev.icerock.moko.parcelize.Parcelable

@Parcelize
data class Location(
    val latitude: Double,
    val longitude: Double,
) : Parcelable
