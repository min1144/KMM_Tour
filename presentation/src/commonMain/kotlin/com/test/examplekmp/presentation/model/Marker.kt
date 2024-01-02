package com.test.examplekmp.presentation.model

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.test.examplekmp.domain.entity.MapItem
import com.test.examplekmp.domain.entity.map.LatLng
import com.test.examplekmp.presentation.extension.getThumbnailUrl

sealed interface Marker {

    val name: String

    val contentId: String

    val contentTypeId: String

    val latLng: LatLng

    val select: Boolean

    val thumbnailUrl: String

    @Parcelize
    data class PlaceMarker(
        override val name: String,
        val address: String,
        val lat: Double,
        val lng: Double,
        override val contentId: String,
        override val contentTypeId: String,
        override val select: Boolean,
        override val thumbnailUrl: String
    ) : Marker, Parcelable {

        override val latLng: LatLng
            get() = LatLng(lat, lng)
    }

    companion object {

        fun from(value: MapItem, select: Boolean): PlaceMarker {
            return PlaceMarker(
                name = value.title,
                address = value.addr1,
                lat = value.mapy.toDouble(),
                lng = value.mapx.toDouble(),
                contentId = value.contentid,
                contentTypeId = value.contenttypeid,
                select = select,
                thumbnailUrl = value.firstimage.getThumbnailUrl()
            )
        }

        fun Marker.copy(select: Boolean): Marker {
            return (this as PlaceMarker).copy(select = select)
        }
    }
}