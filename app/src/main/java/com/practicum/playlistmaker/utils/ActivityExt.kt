package com.practicum.playlistmaker.utils

import android.app.Activity
import android.content.res.Configuration

// реализация проверки использования ресурсов тёмной темы:
val Activity.isUsingNightModeResources : Boolean
    get()
    {
        return when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK)
        {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
}
