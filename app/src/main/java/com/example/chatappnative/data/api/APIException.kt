package com.example.chatappnative.data.api

class APIException(
    val code: Int,
    override var message: String = ""
) : Exception()