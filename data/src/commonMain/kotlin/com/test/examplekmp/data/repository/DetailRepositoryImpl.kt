package com.test.examplekmp.data.repository

import com.test.examplekmp.data.network.ApiService
import com.test.examplekmp.domain.entity.DetailItem
import com.test.examplekmp.domain.entity.SendDetail
import com.test.examplekmp.domain.entity.base.response.BaseResponse
import com.test.examplekmp.domain.repository.DetailRepository
import kotlinx.coroutines.flow.Flow

class DetailRepositoryImpl constructor(private val service: ApiService): DetailRepository {

    override fun getDetail(param: SendDetail): Flow<BaseResponse<DetailItem>> {
        return networkFlow {
            service.getDetail(param).successBody()
        }
    }
}