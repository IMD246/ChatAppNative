package com.example.chatappnative.data.model

data class UserPresenceSocketModel(
    val userId: String? = null,
    val presence: Boolean = false,
)