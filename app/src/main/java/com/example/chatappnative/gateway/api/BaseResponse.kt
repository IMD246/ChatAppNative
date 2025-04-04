package com.example.chatappnative.gateway.api

data class BaseResponse<T>(
    val data: T?,
    val message: String = "",
    val code: Int = 0,
)