package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.search.repository.SearchRepository
import com.practicum.playlistmaker.domain.track.model.Track

class SearchInteractorImpl(
    private val repository: SearchRepository
): SearchInteractor {
    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)
    override fun clearHistory() = repository.clearHistory()
    override fun getHistory(): ArrayList<Track> = repository.getHistory()
}