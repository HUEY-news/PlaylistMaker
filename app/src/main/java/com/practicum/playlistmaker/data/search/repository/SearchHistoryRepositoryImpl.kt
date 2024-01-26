package com.practicum.playlistmaker.data.search.repository

import com.practicum.playlistmaker.data.search.api.SearchHistoryStorage
import com.practicum.playlistmaker.domain.search.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.track.model.Track

class SearchHistoryRepositoryImpl(
    private val storage: SearchHistoryStorage
): SearchHistoryRepository {
    override fun addTrackToHistory(track: Track) = storage.addTrackToHistory(track)
    override fun clearHistory() = storage.clearHistory()
    override fun getHistory(): ArrayList<Track> = storage.getHistory()
}