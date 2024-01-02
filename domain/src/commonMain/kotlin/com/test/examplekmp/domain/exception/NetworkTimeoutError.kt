package com.test.examplekmp.domain.exception

data class NetworkTimeoutError(val throwable: Throwable) : Exception()