package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ChatModel(
    val lastMessage: String,
    val name: String,
    val presence: Boolean,
    val timeLastMessage: String,
    val typeMessage: String,
    val urlImage: String,
    val users: List<String>,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
) {

    fun getDateTimeLastMessage(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(timeLastMessage)

        return parseToUtc
    }

    fun getDateTimePresence(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(presenceTimestamp)

        return parseToUtc

    }
}