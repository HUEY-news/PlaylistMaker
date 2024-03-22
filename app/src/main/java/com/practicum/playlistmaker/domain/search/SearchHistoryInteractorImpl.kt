package com.practicum.playlistmaker.domain.search

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
): SearchHistoryInteractor {
    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)
    override fun clearHistory() = repository.clearHistory()
    override fun getHistory(): ArrayList<Track> = repository.getHistory()
}