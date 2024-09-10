package com.example.chatappnative.data.param

import com.google.gson.annotations.SerializedName

data class UpdateFriendStatusParam(
    @SerializedName("friend_id") val friendId: String = "",
    @SerializedName("status") val status: Int = 0,
)