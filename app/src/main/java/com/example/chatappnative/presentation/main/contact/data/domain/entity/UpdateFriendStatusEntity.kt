package com.example.chatappnative.presentation.main.contact.data.domain.entity

import com.google.gson.annotations.SerializedName

data class UpdateFriendStatusEntity(
    @SerializedName("user_status") val userStatus: Int = 0,
)