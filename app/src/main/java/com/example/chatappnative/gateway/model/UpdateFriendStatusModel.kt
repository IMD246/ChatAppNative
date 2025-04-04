package com.example.chatappnative.gateway.model

import com.google.gson.annotations.SerializedName

data class UpdateFriendStatusModel(
    @SerializedName("user_status") val userStatus: Int = 0,
)