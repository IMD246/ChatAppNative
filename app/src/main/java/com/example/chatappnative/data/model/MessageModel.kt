package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class MessageModel(
    @SerializedName("message") val message: String = "",
    val isMine: Boolean = false,
    @SerializedName("stampTimeMessage") val timeStamp: String = "",
    @SerializedName("messageStatus") val status: String = "not-sent",
    val uuid: String = "",
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("typeMessage") val type: String = "text",
    val senderName: String = "",
    @SerializedName("avatar") val senderAvatar: String = "",
) {
    fun getDateTimeMessage(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(timeStamp)

        return parseToUtc
    }
}