package com.example.chatappnative.data.model

data class UserPresence(
    val presence: Boolean = false,
    val presenceTimeStamp: String = "",
    val userID: String = "",
)