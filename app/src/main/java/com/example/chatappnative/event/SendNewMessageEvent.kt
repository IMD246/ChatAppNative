package com.example.chatappnative.event

import com.example.chatappnative.data.model.MessageModel
import com.google.gson.annotations.SerializedName

data class NewMessageEvent(
    @SerializedName("chatMessage") val message: MessageModel,
)