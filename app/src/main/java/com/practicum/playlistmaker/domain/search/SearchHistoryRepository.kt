package com.practicum.playlistmaker.domain.search

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): Flow<ArrayList<Track>>
}