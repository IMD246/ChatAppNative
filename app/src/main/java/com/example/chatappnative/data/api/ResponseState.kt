package com.example.chatappnative.data.api

sealed class ResponseState<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : ResponseState<T>(data)
    class Success<T>(data: T?, message: String? = null) : ResponseState<T>(data, message)
    class Error<T>(data: T? = null, message: String) : ResponseState<T>(data, message)
}