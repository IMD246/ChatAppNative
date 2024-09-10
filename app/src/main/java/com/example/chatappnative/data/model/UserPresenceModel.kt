package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class UserPresenceModel(
    @SerializedName("presence") val presence: Boolean = false,
    @SerializedName("presenceTimeStamp") val presenceTimeStamp: String = "",
    @SerializedName("userID") val userID: String = "",
)