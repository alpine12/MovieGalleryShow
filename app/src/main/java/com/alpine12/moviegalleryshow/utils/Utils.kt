package com.alpine12.moviegalleryshow.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun DateFormat(date: String, input: String, output: String): String {
        var format = SimpleDateFormat(input, Locale.getDefault())
        val newDate: Date? = format.parse(date)
        format = SimpleDateFormat(output, Locale.getDefault())
        return format.format(newDate!!)
    }
}