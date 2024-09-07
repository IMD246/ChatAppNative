package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class MessageModel(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("message") val message: String = "",
    @SerializedName("stampTimeMessage") val timeStamp: String = "",
    @SerializedName("typeMessage") val type: String = "text",
    @SerializedName("messageStatus") val status: String = "not-sent",
    @SerializedName("avatar") val senderAvatar: String = "",
    @SerializedName("isMine") val isMine: Boolean = false,
    val senderName: String = "",
) {
    fun getDateTimeMessage(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(timeStamp)

        return parseToUtc
    }
}