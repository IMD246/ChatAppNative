package com.example.chatappnative.data

data class BaseResponse<T>(
    val data: T?,
    val message: String = "",
    val code: Int = 0,
)