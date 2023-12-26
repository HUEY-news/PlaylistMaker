package com.practicum.playlistmaker.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import java.util.Locale

// УДОБНЫЙ СПОСОБ ПРЕОБРАЗОВАТЬ DP В PX:
fun pixelConverter(dp: Float, context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()


// ПЕРЕВОД МИЛИСЕКУНД В УДОБОЧИТАЕМЫЙ ФОРМАТ ММ:SS
fun trackTimeFormat(millis: Int): String{
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
}
