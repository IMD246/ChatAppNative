package com.example.chatappnative.data.param

import com.google.gson.annotations.SerializedName

data class UserTypingParam(
    @SerializedName("chatID") val chatID: String = "",
    @SerializedName("userID") val userID: String = "",
    @SerializedName("senderAvatar") val senderAvatar: String = "",
    @SerializedName("senderName") val senderName: String = "",
)