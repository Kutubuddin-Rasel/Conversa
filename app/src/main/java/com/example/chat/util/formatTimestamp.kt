package com.example.chat.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatTimestampTotime(timestamp: Timestamp): String {

    // Define the input date format
    val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH)
    inputFormat.timeZone = TimeZone.getTimeZone("GMT")

    // Parse the input date string into a Date object
    val date: Date = inputFormat.parse(timestamp.toDate().toString()) ?: return "Invalid date"

    // Define the output time format in 12-hour format with AM/PM
    val outputFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)

    // Format the Date object into the desired output string
    val formattedTime = outputFormat.format(date)

    // Convert AM/PM to lowercase
    return formattedTime.lowercase(Locale.ENGLISH)

}