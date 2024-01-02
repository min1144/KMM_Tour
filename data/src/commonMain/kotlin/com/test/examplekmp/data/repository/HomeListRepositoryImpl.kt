package com.test.examplekmp.data.repository

import com.test.examplekmp.data.network.ApiService
import com.test.examplekmp.domain.entity.Event
import com.test.examplekmp.domain.entity.Hotel
import com.test.examplekmp.domain.entity.Keyword
import com.test.examplekmp.domain.entity.SendEvent
import com.test.examplekmp.domain.entity.SendHotel
import com.test.examplekmp.domain.entity.SendKeyword
import com.test.examplekmp.domain.entity.base.response.BaseResponse
import com.test.examplekmp.domain.repository.HomeListRepository
import kotlinx.coroutines.flow.Flow

class HomeListRepositoryImpl constructor(private val service: ApiService) : HomeListRepository {

    override fun getKeywordList(param: SendKeyword): Flow<BaseResponse<Keyword>> {
        return networkFlow {
            service.getKeywordList(param).successBody()
        }
    }

    override fun getEventList(param: SendEvent): Flow<BaseResponse<Event>> {
        return networkFlow {
            service.getEventList(param).successBody()
        }
    }

    override fun getHotelList(param: SendHotel): Flow<BaseResponse<Hotel>> {
        return networkFlow {
            service.getHotelList(param).successBody()
        }
    }
}