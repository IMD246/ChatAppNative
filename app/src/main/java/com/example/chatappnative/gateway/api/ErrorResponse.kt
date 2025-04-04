package com.example.chatappnative.gateway.api

data class ErrorResponse(
    val message: String = "",
    val code: Int = 0,
)