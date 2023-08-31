package com.hypersoft.mediastore.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

object GeneralUtils {
    fun readableFileSize(sizeStamp: Long): String {
        try {
            val size = sizeStamp
            if (size <= 0) return "0"
            val units = arrayOf("B", "kB", "MB", "GB", "TB")
            val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]

        } catch (ex: Exception) {
            return "0"
        }

    }

    fun readableDuration(songDurationMillis: Long): String {
        var minutes = songDurationMillis / 1000 / 60
        val seconds = songDurationMillis / 1000 % 60
        return if (minutes < 60) {
            String.format(
                Locale.getDefault(),
                "%02d:%02d",
                minutes,
                seconds
            )
        } else {
            val hours = minutes / 60
            minutes %= 60
            String.format(
                Locale.getDefault(),
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun readableTimeStamp(date: Long, format: String): String {
        return try {
            val dateFormat = SimpleDateFormat(format)
            dateFormat.format(Date(date * 1000))
        } catch (ex: Exception) {
            Log.e("SongViewModelTAG", "readableTimeStamp: ")
            "05,December,2018"
        }

    }
}
