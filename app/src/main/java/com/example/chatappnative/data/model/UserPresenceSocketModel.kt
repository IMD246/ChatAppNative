package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class UserPresenceSocketModel(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("presence") val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
)