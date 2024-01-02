package com.test.examplekmp.domain.repository

import com.test.examplekmp.domain.entity.DetailItem
import com.test.examplekmp.domain.entity.SendDetail
import com.test.examplekmp.domain.entity.base.response.BaseResponse
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun getDetail(param: SendDetail): Flow<BaseResponse<DetailItem>>
}