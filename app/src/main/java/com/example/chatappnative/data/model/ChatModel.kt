package com.example.chatappnative.data.model

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

data class ChatModel(
    val lastMessage: String,
    val name: String,
    val presence: Boolean,
    val timeLastMessage: String,
    val typeMessage: String,
    val urlImage: String,
    val users: List<String>
) {

    @SuppressLint("SimpleDateFormat")
    fun getDateTimeLastMessage(): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val tz = TimeZone.getDefault()
        formatter.timeZone = tz;

        val date = formatter.parse(timeLastMessage)

        return date as Date
    }
}