package com.test.examplekmp.domain.exception

data class NetworkConnectionError(val throwable: Throwable) : Exception()