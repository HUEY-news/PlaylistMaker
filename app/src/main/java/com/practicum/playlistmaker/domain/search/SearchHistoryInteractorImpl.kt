package com.practicum.playlistmaker.domain.search

import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
): SearchHistoryInteractor {
    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)
    override fun clearHistory() = repository.clearHistory()
    override fun getHistory(): Flow<ArrayList<Track>> = repository.getHistory()
}