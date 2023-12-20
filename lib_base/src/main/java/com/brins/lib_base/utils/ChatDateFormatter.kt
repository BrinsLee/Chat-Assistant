package com.brins.lib_base.utils

import io.getstream.chat.android.ui.common.helper.DateFormatter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ChatDateFormatter @Inject constructor(): DateFormatter {

    private val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    private val timeFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    override fun formatDate(date: Date?): String {
        return dateFormat.format(date)
    }

    override fun formatTime(date: Date?): String {
        return timeFormat.format(date)
    }


    fun formatDateTime(date: Date?): String {
        return "${formatDate(date)} ${formatTime(date)}"
    }

}