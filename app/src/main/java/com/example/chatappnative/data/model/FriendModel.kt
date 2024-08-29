package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date

data class FriendModel(
    val id: String = "",
    val name: String = "",
    val urlImage: String? = "",
    val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
) {

    fun getDateTimePresence(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(presenceTimestamp)

        return parseToUtc
    }
}