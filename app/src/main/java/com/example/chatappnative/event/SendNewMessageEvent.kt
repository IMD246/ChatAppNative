package com.example.chatappnative.event

import com.example.chatappnative.gateway.model.MessageModel
import com.google.gson.annotations.SerializedName

data class NewMessageEvent(
    @SerializedName("chatMessage") val message: MessageModel,
)