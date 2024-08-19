package com.example.chatappnative.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

object DateFormatUtil {
    val DATE_TIME_FORMAT: String = "yyyy-MM-ddTHH:mm:ssZ"
    val DATE_TIME_FORMAT2: String = "dd/MM/yyyy HH:mm"
    val DATE_TIME_FORMAT3: String = "HH:mm dd/MM/yyyy"
    val DATE_TIME_FORMAT4: String = "HH:mm - dd/MM/yyyy"
    val DATE_FORMAT: String = "dd/MM/yyyy"
    val DATE_FORMAT2: String = "dd-MM-yyyy"
    val DATE_OF_WEEK_FORMAT: String = "EEE, dd/MM"
    val TIME_FORMAT: String = "HH:mm"

    @SuppressLint("SimpleDateFormat")
    fun dateMessageFormat(value: Date): String {
        val currentDate = getCurrentLocalDate()

        // calculate difference milliseconds
        val diffInMillis = currentDate.time - value.time

        // get seconds in milliseconds
        val seconds = diffInMillis / 1000

        if (seconds.toInt() == 0) {
            return "Just now"
        }

        // if seconds is less than a minute
        if (seconds in 1..59) {
            return "$seconds s ago"
        }

        // if seconds is less than an hour
        if (seconds in 60..86399) {
            return SimpleDateFormat(TIME_FORMAT).format(value)
        }

        return SimpleDateFormat(DATE_FORMAT).format(value)
    }

    fun isDiffSecondsMoreThanAMinute(value: Date): Boolean {
        val currentDate = Date()

        // calculate difference milliseconds
        val diffInMillis = currentDate.time - value.time

        // get seconds in milliseconds
        val seconds = diffInMillis / 1000

        // if seconds is less than a minute
        if (seconds in 0..59) {
            return false
        }

        return true
    }

    fun getCurrentLocalDate(): Date {
        val date = Date()

        val getFormattedDate = getFormattedDate(date)

        val toLocalCurrentDate = parseToLocalDate(getFormattedDate)

        return toLocalCurrentDate
    }

    @SuppressLint("SimpleDateFormat")
    fun parseToLocalDate(value: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val tz = TimeZone.getDefault()
        formatter.timeZone = tz;

        val date = formatter.parse(value) as Date

        return date
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedDate(value: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(value)
    }
}