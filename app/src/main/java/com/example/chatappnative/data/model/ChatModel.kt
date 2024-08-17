package com.example.chatappnative.data.model

import java.util.Date

data class ChatModel(
    val name: String,
    val type: String,
    val typeMessage: String,
    val lastMessage: String,
    val createdDate: Date,
    val image: String = "",
)