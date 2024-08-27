package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class ContactModel(
    val id: String = "",
    val name: String = "",
    val status: Int = 1,
    val urlImage: String = "",
    val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
)