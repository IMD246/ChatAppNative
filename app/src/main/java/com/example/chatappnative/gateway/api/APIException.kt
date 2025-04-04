package com.example.chatappnative.gateway.api

class APIException(
    val code: Int,
    override var message: String = ""
) : Exception()