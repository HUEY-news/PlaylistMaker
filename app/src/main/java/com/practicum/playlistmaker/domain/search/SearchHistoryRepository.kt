package com.practicum.playlistmaker.domain.search

interface SearchHistoryRepository {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}