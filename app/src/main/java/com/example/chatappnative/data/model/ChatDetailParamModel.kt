package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChatDetailParamModel(
    @SerializedName("page_size_message") val pageSizeMessage: Int = 15,
    @SerializedName("type") val type: String? = "personal",
    @SerializedName("room_id") val chatID: String? = null,
    @SerializedName("list_user_id") val listUserID: List<String>? = null,
) : Serializable