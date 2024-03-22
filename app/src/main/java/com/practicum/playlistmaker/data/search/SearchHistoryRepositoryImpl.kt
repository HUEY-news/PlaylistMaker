package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow

class SearchHistoryRepositoryImpl(
    private val storage: SearchHistoryStorage
): SearchHistoryRepository {
    override fun addTrackToHistory(track: Track) = storage.addTrackToHistory(track)
    override fun clearHistory() = storage.clearHistory()
    override fun getHistory(): Flow<ArrayList<Track>> = storage.getHistory()
}