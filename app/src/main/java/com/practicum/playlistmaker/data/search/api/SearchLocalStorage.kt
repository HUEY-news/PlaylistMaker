package com.practicum.playlistmaker.data.search.api

import com.practicum.playlistmaker.domain.track.model.Track

interface SearchLocalStorage {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}