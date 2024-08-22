package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class UpdateFriendStatusParamModel(
    @SerializedName("friend_id") val friendId: String,
    @SerializedName("status") val status: Int
)