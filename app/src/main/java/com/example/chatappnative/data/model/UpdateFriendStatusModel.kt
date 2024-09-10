package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class UpdateFriendStatusModel(
    @SerializedName("user_status") val userStatus: Int = 0,
)