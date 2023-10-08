package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue

// УДОБНЫЙ СПОСОБ ПРЕОБРАЗОВАТЬ DP В PX:
fun pixelConverter(dp: Float, context: Context) =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()

