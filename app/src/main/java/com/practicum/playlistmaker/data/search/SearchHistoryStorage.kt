package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.Track

interface SearchHistoryStorage {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}