package com.test.examplekmp.domain.exception

data class ServerResponseError(
    val statusCode: Int,
    val errorResponse: String?,
    val throwable: Throwable
) : Exception()