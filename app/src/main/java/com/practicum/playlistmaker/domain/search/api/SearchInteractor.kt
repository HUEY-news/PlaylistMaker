package com.practicum.playlistmaker.domain.search.api

import com.practicum.playlistmaker.domain.track.model.Track

interface SearchInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}