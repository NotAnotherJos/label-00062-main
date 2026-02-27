package com.example.core.exception

open class NetworkException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)
class NoInternetException(message: String = "No internet connection") : NetworkException(message)
class TimeoutException(message: String = "Request timed out") : NetworkException(message)
class ServerException(val code: Int, message: String) : NetworkException("Server error $code: $message")
class ClientException(val code: Int, message: String) : NetworkException("Client error $code: $message")
