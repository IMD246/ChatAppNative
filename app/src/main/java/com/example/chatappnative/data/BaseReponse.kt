package com.example.chatappnative.data

data class BaseReponse<T>(
    val data: T?,
    val message: String = "",
    val code: Int = 0,
)