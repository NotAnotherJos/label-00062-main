package com.example.core.network.adapter

import com.example.core.exception.NetworkException
import com.example.core.exception.ClientException
import com.example.core.exception.ServerException
import com.example.core.exception.NoInternetException
import com.example.core.exception.TimeoutException
import com.example.core.network.result.NetworkResult
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import java.io.IOException
import java.net.UnknownHostException

/**
 * A safe API call wrapper that converts suspend functions into Flow<NetworkResult<T>>.
 * Automatically handles loading state and exception mapping.
 */
fun <T> safeApiCall(apiCall: suspend () -> T): Flow<NetworkResult<T>> = flow<NetworkResult<T>> {
    emit(NetworkResult.Success(apiCall()))
}.onStart {
    emit(NetworkResult.Loading)
}.catch { e ->
    val networkException = when (e) {
        is NetworkException -> e
        is ResponseException -> {
            val status = e.response.status.value
            val errorBody = runCatching { e.response.bodyAsText() }.getOrDefault("Unknown error")
            when (status) {
                in 400..499 -> ClientException(status, errorBody)
                in 500..599 -> ServerException(status, errorBody)
                else -> ServerException(status, "Unexpected error: $errorBody")
            }
        }
        is UnknownHostException, is java.net.ConnectException -> NoInternetException()
        is io.ktor.client.network.sockets.SocketTimeoutException, is io.ktor.client.network.sockets.ConnectTimeoutException -> TimeoutException()
        else -> NetworkException("Unknown error", e)
    }
    emit(NetworkResult.Error(exception = networkException, message = networkException.message))
}
