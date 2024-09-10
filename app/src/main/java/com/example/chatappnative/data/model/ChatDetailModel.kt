package com.example.chatappnative.data.model

import com.example.chatappnative.data.param.TypeChat
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ChatDetailModel(
    @SerializedName("_id") val id: String = "",
    @SerializedName("lastMessage") val lastMessage: String = "",
    @SerializedName("nameChat") val nameChat: String = "",
    @SerializedName("timeLastMessage") val timeLastMessage: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("userIDLastMessage") val userIDLastMessage: String = "",
    @SerializedName("userNameLastMessage") val userNameLastMessage: String = "",
    @SerializedName("typeMessage") val typeMessage: String = "",
    @SerializedName("users") val usersPresence: List<UserPresenceModel> = arrayListOf(),
    @SerializedName("messages") val messages: List<MessageModel> = arrayListOf(),
    @SerializedName("totalPages") val totalPages: Int = 0
) {
    fun getPresence(): Boolean {
        return usersPresence.any { it.presence }
    }

    fun getDateTimePresence(): Date {
        var getDate = ""

        if (usersPresence.isEmpty()) {
            return DateFormatUtil.parseUtcToDate(getDate)
        }

        var usersPresenceData = usersPresence

        usersPresenceData = usersPresenceData.sortedByDescending {
            it.presenceTimeStamp
        }

        val getFirstTimeStampPresence = usersPresenceData.first()

        getDate = getFirstTimeStampPresence.presenceTimeStamp

        val parseToUtc = DateFormatUtil.parseUtcToDate(getDate)

        return parseToUtc
    }

    fun getTypeChat(): TypeChat {
        return when (type) {
            "group" -> {
                TypeChat.GROUP
            }

            else -> {
                TypeChat.PERSONAL
            }
        }
    }
}