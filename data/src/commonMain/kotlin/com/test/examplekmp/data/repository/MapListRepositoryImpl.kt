package com.test.examplekmp.data.repository

import com.test.examplekmp.data.network.ApiService
import com.test.examplekmp.domain.entity.MapItem
import com.test.examplekmp.domain.entity.SendMap
import com.test.examplekmp.domain.entity.base.response.BaseResponse
import com.test.examplekmp.domain.repository.MapListRepository
import kotlinx.coroutines.flow.Flow

class MapListRepositoryImpl constructor(private val service: ApiService): MapListRepository {

    override fun getMapList(param: SendMap): Flow<BaseResponse<MapItem>> {
        return networkFlow {
            service.getMapList(param).successBody()
        }
    }
}