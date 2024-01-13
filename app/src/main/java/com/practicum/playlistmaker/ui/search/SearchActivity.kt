package com.practicum.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.presentation.PlayerController
import com.practicum.playlistmaker.ui.player.PlayerActivity
import com.practicum.playlistmaker.util.Creator

class SearchActivity : AppCompatActivity() {

    private val searchHistory = SearchHistory(App.sharedPreferences)
    private val searchAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            searchHistory.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(PlayerController.TRACK_ID, track)
            startActivity(intent)
        }
    }
    private val historyAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(PlayerController.TRACK_ID, track)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val trackSearchController = Creator.provideTrackSearchController (
        this,
        searchAdapter,
        historyAdapter,
        searchHistory
    )

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        trackSearchController.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        trackSearchController.onDestroy()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, SearchActivity.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}