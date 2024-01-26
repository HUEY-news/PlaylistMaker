package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.repository.SearchHistoryRepository
import com.practicum.playlistmaker.domain.track.model.Track

class SearchHistoryInteractorImpl(
    private val repository: SearchHistoryRepository
): SearchInteractor {
    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)
    override fun clearHistory() = repository.clearHistory()
    override fun getHistory(): ArrayList<Track> = repository.getHistory()
}