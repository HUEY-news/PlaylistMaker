package com.practicum.playlistmaker.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.practicum.playlistmaker.presentation.search.SearchActivity

// TODO: В класс вынесена логика с дебаунсом на клики. Но мне не нравится, что сюда передан контекст,
//  который к тому же ещё кастится к SearchActivity. Во-первых это утёчка памяти,
//  во-вторых класс становится не таким универсальным, т.к. теперь он привязан к поиску.
//  Я бы всё, что касается дебаунса оставил в этом классе, а всё,
//  что касается поиска - вынес в SearchActivity
class Debouncer(context: Context?) {

    constructor() : this (context = null)

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { (context as SearchActivity).searchRequest() }

    fun clickDebounce() : Boolean {
        Log.d("myLOG", "clickDebounce() activated")
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun searchDebounce() {
        Log.d("myLOG", "searchDebounce() activated")
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}