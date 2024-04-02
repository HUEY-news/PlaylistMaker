package com.practicum.playlistmaker.domain.search

import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): Flow<List<Track>>
}