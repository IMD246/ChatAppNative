package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class FriendModel(
    @SerializedName("id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("name") val name: String = "",
    @SerializedName("urlImage") val urlImage: String? = "",
    @SerializedName("presence") val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
) {

    fun getDateTimePresence(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(presenceTimestamp)

        return parseToUtc
    }
}