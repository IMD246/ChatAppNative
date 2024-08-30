package com.example.chatappnative.util

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatUtil {
    const val DATE_TIME_FORMAT: String = "yyyy-MM-ddTHH:mm:ssZ"
    const val DATE_TIME_FORMAT2: String = "dd/MM/yyyy HH:mm"
    const val DATE_TIME_FORMAT3: String = "HH:mm dd/MM/yyyy"
    const val DATE_TIME_FORMAT4: String = "HH:mm - dd/MM/yyyy"
    const val DATE_FORMAT: String = "dd/MM/yyyy"
    const val DATE_FORMAT2: String = "dd-MM-yyyy"
    const val DATE_FORMAT3: String = "yyyy-MM-dd"
    const val DATE_OF_WEEK_FORMAT: String = "EEE, dd/MM"
    const val TIME_FORMAT: String = "HH:mm"

    @SuppressLint("SimpleDateFormat")
    fun dateMessageFormat(value: Date): String {
        val currentDate = Date()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val tz = TimeZone.getDefault()
        formatter.timeZone = tz;

        val formattedDate = formatter.format(currentDate)
        val date = formatter.parse(formattedDate) as Date

        // calculate difference milliseconds
        val diffInMillis = date.time - value.time

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

    fun isDiffSecondsLessThanAMinute(value: Date): Boolean {
        val currentDate = Date()

        // calculate difference milliseconds
        val diffInMillis = currentDate.time - value.time

        // get seconds in milliseconds
        val seconds = diffInMillis / 1000

        // if seconds is less than a minute
        return seconds in 0..59
    }

    fun isDiffSecondsMoreThanAnHour(value: Date): Boolean {
        val currentDate = Date()

        // calculate difference milliseconds
        val diffInMillis = currentDate.time - value.time

        // get seconds in milliseconds
        val seconds = diffInMillis / 1000

        // if seconds is less than a minute
        return seconds in 60..3599
    }

    fun isDiffSecondsMoreThanADay(value: Date): Boolean {
        val currentDate = Date()

        // calculate difference milliseconds
        val diffInMillis = currentDate.time - value.time

        // get seconds in milliseconds
        val seconds = diffInMillis / 1000

        // if seconds is less than a minute
        return seconds in 3600..86399
    }

    fun getDelayDiffMilliseconds(value: Date): Long {
        if (isDiffSecondsLessThanAMinute(value)) {
            return 1000L
        }

        if (isDiffSecondsMoreThanAnHour(value)) {
            return 60000L
        }

        return 3600000L
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentLocalDate(): Date {
        val currentDate = Date()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val tz = TimeZone.getDefault()
        formatter.timeZone = tz;

        val formattedDate = formatter.format(currentDate)
        val date = formatter.parse(formattedDate) as Date

        return date
    }

    @SuppressLint("SimpleDateFormat")
    fun parseToLocalDate(value: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val tz = TimeZone.getDefault()
        formatter.timeZone = tz;

        val date = formatter.parse(value) as Date

        return date
    }

    @SuppressLint("SimpleDateFormat")
    fun getFormattedDate(value: Date, format: String = DATE_FORMAT3): String {
        val formatter = SimpleDateFormat(format)
        return formatter.format(value)
    }

    @SuppressLint("SimpleDateFormat")
    fun presenceFormat(value: Date): String {
        // Get the current date
        val currentDate = Date()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val formattedDate = formatter.format(currentDate)

        val tz = TimeZone.getDefault()

        formatter.timeZone = tz;

        val date = formatter.parse(formattedDate) as Date

        // calculate difference milliseconds
        val diffInMillis = date.time - value.time

        // get seconds in milliseconds
        val seconds = diffInMillis / 1000

        // if seconds is less than a minute
        if (seconds in 0..59) {
            return "${seconds}s"
        }

        // if seconds is less than an hour
        if (seconds in 60..3599) {
            val minutes = (seconds / 60).toInt()
            return "${minutes}m"
        }

        if (seconds in 3600..86399) {
            val hours = (seconds / 3600).toInt()
            return "${hours}h"
        }

        if (seconds in 86400..1209600) {
            val days = (seconds / 86400).toInt()
            return "${days}d"
        }

        return ""
    }

    @SuppressLint("SimpleDateFormat")
    fun presenceMessageFormat(value: Date): String {
        // Get the current date
        val currentDate = Date()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val formattedDate = formatter.format(currentDate)

        val tz = TimeZone.getDefault()

        formatter.timeZone = tz;

        val date = formatter.parse(formattedDate) as Date

        // calculate difference milliseconds
        val diffInMillis = date.time - value.time

        // get seconds in milliseconds
        val seconds = diffInMillis / 1000

        // if seconds is less than a minute
        if (seconds in 0..59) {
            return "Hoạt động $seconds giây trước"
        }

        // if seconds is less than an hour
        if (seconds in 60..3599) {
            val minutes = (seconds / 60).toInt()
            return "Hoạt động $minutes phút trước"
        }

        if (seconds in 3600..86399) {
            val hours = (seconds / 3600).toInt()
            return "Hoạt động $hours giờ trước"
        }

        if (seconds in 86400..1209600) {
            val days = (seconds / 86400).toInt()
            return "Hoạt động $days ngày trước"
        }

        return ""
    }

    fun parseUtcToDate(utcTimestamp: String): Date {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android O and above
            val utcDateTime =
                ZonedDateTime.parse(utcTimestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME)
            Date.from(utcDateTime.toInstant())
        } else {
            // For versions below Android O
            val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            utcFormat.parse(utcTimestamp)!!
        }
    }
}