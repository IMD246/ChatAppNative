package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ContactModel(
    val uuid: String = UUID.randomUUID().toString(),
    val id: String = "",
    val name: String = "",
    val status: Int = 1,
    val urlImage: String = "",
    val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
)