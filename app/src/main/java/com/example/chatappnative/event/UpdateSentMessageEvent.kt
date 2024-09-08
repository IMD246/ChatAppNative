package com.example.chatappnative.event

import com.google.gson.annotations.SerializedName

data class UpdateSentMessageEvent(
    @SerializedName("idMessage") val idMessage: String = "",
    @SerializedName("uuid") val uuid: String = "",
    @SerializedName("statusMessage") val statusMessage: String = "sent"
)