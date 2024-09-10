package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ContactModel(
    @SerializedName("id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("name") val name: String = "",
    @SerializedName("status") val status: Int = 1,
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("presence") val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
)