package com.test.examplekmp.data.repository

import com.test.examplekmp.data.util.createNetworkErrorException
import com.test.examplekmp.domain.entity.base.response.BaseResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

fun <T> networkFlow(response: suspend () -> T): Flow<T> = flow {
    emit(response.invoke())
}.catch { e ->
    createNetworkErrorException(e)
}


suspend inline fun <reified T> HttpResponse.successBody(): T {
    val r: T = this.body()
    if(r is BaseResponse<*>) {
        val header = r.response.header
        val success = header.resultMsg == "OK"
        if (!success) {
            throw Exception(r.response.body.toString())
        }
    }
    return r
}