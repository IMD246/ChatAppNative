package com.example.chatappnative.data

data class BaseReponse(
    val data: Any,
    val message: String = "",
    val code: Int = 0,
)