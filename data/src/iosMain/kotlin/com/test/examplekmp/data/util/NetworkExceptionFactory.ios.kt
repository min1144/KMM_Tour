package com.test.examplekmp.data.util

import com.test.examplekmp.domain.exception.NetworkConnectionError
import com.test.examplekmp.domain.exception.NetworkTimeoutError
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.util.network.UnresolvedAddressException
import platform.Foundation.NSURLErrorNotConnectedToInternet

actual fun createNetworkErrorException(throwable: Throwable): Exception {
    when (throwable) {
        is HttpRequestTimeoutException, is ConnectTimeoutException, is SocketTimeoutException -> {
            throw NetworkTimeoutError(throwable)
        }

        else -> {
            if (throwable.isNetworkConnectionError()) {
                throw NetworkConnectionError(throwable)
            } else {
                throw throwable
            }
        }
    }
}

private fun Throwable.isNetworkConnectionError(): Boolean {
    return when (this) {
        is UnresolvedAddressException -> true
        is DarwinHttpRequestException -> {
            origin.code == NSURLErrorNotConnectedToInternet
        }

        else -> false
    }
}