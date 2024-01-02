package com.test.examplekmp.domain.repository

import com.test.examplekmp.domain.entity.Event
import com.test.examplekmp.domain.entity.Hotel
import com.test.examplekmp.domain.entity.Keyword
import com.test.examplekmp.domain.entity.SendEvent
import com.test.examplekmp.domain.entity.SendHotel
import com.test.examplekmp.domain.entity.SendKeyword
import com.test.examplekmp.domain.entity.base.response.BaseResponse
import kotlinx.coroutines.flow.Flow

interface HomeListRepository {

    fun getKeywordList(param: SendKeyword): Flow<BaseResponse<Keyword>>

    fun getEventList(param: SendEvent): Flow<BaseResponse<Event>>

    fun getHotelList(param: SendHotel): Flow<BaseResponse<Hotel>>
}