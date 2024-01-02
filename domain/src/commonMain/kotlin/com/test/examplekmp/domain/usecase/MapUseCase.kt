package com.test.examplekmp.domain.usecase

import com.test.examplekmp.domain.entity.MapItem
import com.test.examplekmp.domain.entity.SendMap
import com.test.examplekmp.domain.repository.MapListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MapUseCase constructor(
    private val mapListRepository: MapListRepository
) {

    fun getMapList(param: SendMap): Flow<List<MapItem>> {
        return mapListRepository.getMapList(param).map {
            it.response.body.items?.item ?: emptyList()
        }
    }
}