package com.test.examplekmp.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class HomeItem(
    var keyword: List<Keyword> ?= null,
    var hotel: List<Hotel> ?= null,
    var event: List<Event> ?= null,
    var homeHeader: HomeHeader ?= null,
    val viewType: HomeItemViewType,
) {

    override fun hashCode(): Int {
        var result = viewType.hashCode()
        result = 31 * result + keyword.hashCode()
        result = 31 * result + hotel.hashCode()
        result = 31 * result + event.hashCode()
        result = 31 * result + homeHeader.hashCode()
        return result
    }
}


enum class HomeItemViewType {
    HEADER, PAGER, LIST, GRID
}