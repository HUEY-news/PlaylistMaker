package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryStorage {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): Flow<ArrayList<Track>>
}