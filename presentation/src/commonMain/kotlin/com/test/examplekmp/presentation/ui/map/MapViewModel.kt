package com.test.examplekmp.presentation.ui.map

import com.test.examplekmp.domain.entity.SendMap
import com.test.examplekmp.domain.entity.base.ArrangeType
import com.test.examplekmp.domain.entity.map.LatLng
import com.test.examplekmp.domain.usecase.MapUseCase
import com.test.examplekmp.presentation.base.BaseViewModel
import com.test.examplekmp.presentation.model.Marker
import com.test.examplekmp.presentation.model.Marker.Companion.copy
import com.test.examplekmp.presentation.ui.view.map.DEFAULT_LAT
import com.test.examplekmp.presentation.ui.view.map.DEFAULT_LON
import com.test.examplekmp.presentation.ui.view.map.DEFAULT_RADIUS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class MapViewModel(
    private val mapUseCase: MapUseCase
): BaseViewModel() {

    companion object {
        private const val MAP_REQUEST_COUNT = "30"
    }

    private val _markerList = MutableStateFlow<List<Marker>>(emptyList())

    val markerList: StateFlow<List<Marker>>
        get() = _markerList.asStateFlow()

    private val _selectMarker = MutableStateFlow<Marker?>(null)

    val selectMarker: StateFlow<Marker?>
        get() = _selectMarker.asStateFlow()

    init {
        getMarkerList()
    }

    private fun getMarkerList(mapX: Double = DEFAULT_LON, mapY: Double = DEFAULT_LAT) = ioJob {
        val param = SendMap(
            numOfRows = MAP_REQUEST_COUNT,
            arrange = ArrangeType.IMAGE_CREATED.type,
            mapX = mapX,
            mapY = mapY,
            radius = DEFAULT_RADIUS
        )
        mapUseCase.getMapList(param).map { item ->
            item.map {
                Marker.from(it, select = false)
            }
        }.onStart {
            setLoading(true)
        }.onCompletion {
            setLoading(false)
        }.collectLatest {
            _markerList.emit(it)
        }
    }

    fun selectMarker(marker: Marker?) = defaultJob {
        val newList = _markerList.value.map {
            it.copy(select = it.contentId == marker?.contentId)
        }
        _selectMarker.emit(marker)
        _markerList.emit(newList)
    }

    fun getCurrentMarkerList(latLng: LatLng) {
        _selectMarker.value = null
        getMarkerList(mapX = latLng.longitude, mapY = latLng.latitude)
    }
}