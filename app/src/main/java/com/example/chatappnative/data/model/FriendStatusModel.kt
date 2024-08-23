package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class FriendStatusModel(
    @SerializedName("friend_id") val friendId: String,
    @SerializedName("sender_status") val senderStatus: Int,
    @SerializedName("friend_status") val friendStatus: Int,
    val urlImage: String = "",
)