package com.practicum.playlistmaker.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import java.util.Locale

// удобный способ преобразовать DP в PX:
fun pixelConverter(dp: Float, context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()


// перевод милисекунд в удобочитаемый формат ММ:SS
fun trackTimeFormat(millis: Int): String{
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
}
