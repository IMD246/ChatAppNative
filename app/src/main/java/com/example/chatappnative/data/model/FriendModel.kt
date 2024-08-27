package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class FriendModel(
    val id: String = "",
    val name: String = "",
    val urlImage: String? = "",
    val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
)