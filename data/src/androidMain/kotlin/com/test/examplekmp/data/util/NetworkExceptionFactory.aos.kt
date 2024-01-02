package com.test.examplekmp.data.util

import com.test.examplekmp.domain.exception.NetworkConnectionError
import com.test.examplekmp.domain.exception.NetworkTimeoutError
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

actual fun createNetworkErrorException(throwable: Throwable): Exception {
    when (throwable) {
        is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException -> {
            throw NetworkTimeoutError(throwable)
        }

        is UnresolvedAddressException, is UnknownHostException -> {
            throw NetworkConnectionError(throwable)
        }

        else -> {
            throw throwable
        }
    }
}