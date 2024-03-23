package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.Track

class SearchHistoryRepositoryImpl(
    private val storage: SearchHistoryStorage
): SearchHistoryRepository {
    override fun addTrackToHistory(track: Track) = storage.addTrackToHistory(track)
    override fun clearHistory() = storage.clearHistory()
    override fun getHistory(): ArrayList<Track> = storage.getHistory()
}