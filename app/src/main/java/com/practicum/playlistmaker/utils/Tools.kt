package com.practicum.playlistmaker.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import java.text.DateFormat
import java.util.Date
import java.util.Locale

// удобный способ преобразовать DP в PX:
fun convertPixel(dp: Float, context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()


// перевод милисекунд в удобочитаемый формат ММ:SS
fun convertTime(millis: Int): String{
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
}


// перевод даты в формат YYYY
fun convertDate(date: String): String {
    val dateFormat: DateFormat = java.text.SimpleDateFormat("yyyy-MM-dd")
    val date: Date = dateFormat.parse(date)
    val yearFormat: DateFormat = java.text.SimpleDateFormat("yyyy")
    return yearFormat.format(date)
}
