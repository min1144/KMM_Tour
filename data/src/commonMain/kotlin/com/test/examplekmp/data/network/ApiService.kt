package com.test.examplekmp.data.network

import com.test.examplekmp.data.network.ApiConstants.appendUrl
import com.test.examplekmp.data.util.applyParametersBuilder
import com.test.examplekmp.domain.entity.SendDetail
import com.test.examplekmp.domain.entity.SendEvent
import com.test.examplekmp.domain.entity.SendHotel
import com.test.examplekmp.domain.entity.SendKeyword
import com.test.examplekmp.domain.entity.SendMap
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class ApiService(private val client: HttpClient) {

    suspend fun getKeywordList(param: SendKeyword): HttpResponse {
        return client.get(appendUrl("searchKeyword1")) {
            applyParametersBuilder(param)
        }
    }

    suspend fun getEventList(param: SendEvent): HttpResponse {
        return client.get(appendUrl("searchFestival1")) {
            applyParametersBuilder(param)
        }
    }

    suspend fun getHotelList(param: SendHotel): HttpResponse {
        return client.get(appendUrl("searchStay1")) {
            applyParametersBuilder(param)
        }
    }

    suspend fun getMapList(param: SendMap): HttpResponse {
        return client.get(appendUrl("locationBasedList1")) {
            applyParametersBuilder(param)
        }
    }

    suspend fun getDetail(param: SendDetail): HttpResponse {
        return client.get(appendUrl("detailCommon1")) {
            applyParametersBuilder(param, hashMapOf(
                "MobileOS" to ApiConstants.ETC,
                "MobileApp" to ApiConstants.APP_NAME,
                "_type" to ApiConstants.JSON,
                "serviceKey" to ApiConstants.SERVICE_KEY,
            ))
        }
    }
}