package com.example.chatappnative.event

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class UpdateSentMessageEvent(
    @SerializedName("idMessage") val idMessage: String = UUID.randomUUID().toString(),
    @SerializedName("uuid") val uuid: String = UUID.randomUUID().toString(),
    @SerializedName("statusMessage") val statusMessage: String = "sent"
)