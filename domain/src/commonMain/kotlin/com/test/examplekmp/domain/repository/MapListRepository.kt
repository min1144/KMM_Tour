package com.test.examplekmp.domain.repository

import com.test.examplekmp.domain.entity.MapItem
import com.test.examplekmp.domain.entity.SendMap
import com.test.examplekmp.domain.entity.base.response.BaseResponse
import kotlinx.coroutines.flow.Flow

interface MapListRepository {
    fun getMapList(param: SendMap): Flow<BaseResponse<MapItem>>
}