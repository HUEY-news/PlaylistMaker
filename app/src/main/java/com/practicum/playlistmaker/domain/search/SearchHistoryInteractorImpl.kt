package com.practicum.playlistmaker.domain.search

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryInteractorImpl @Inject constructor(
    private val repository: SearchHistoryRepository
): SearchHistoryInteractor {
    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)
    override fun clearHistory() = repository.clearHistory()
    override fun getHistory(): Flow<List<Track>> = repository.getHistory()
}
