package com.practicum.playlistmaker.domain.search

interface SearchHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}