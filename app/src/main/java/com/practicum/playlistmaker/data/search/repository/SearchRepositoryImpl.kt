package com.practicum.playlistmaker.data.search.repository

import com.practicum.playlistmaker.data.search.api.SearchLocalStorage
import com.practicum.playlistmaker.domain.search.repository.SearchRepository
import com.practicum.playlistmaker.domain.track.model.Track

class SearchRepositoryImpl(
    private val storage: SearchLocalStorage
): SearchRepository {
    override fun addTrackToHistory(track: Track) = storage.addTrackToHistory(track)
    override fun clearHistory() = storage.clearHistory()
    override fun getHistory(): ArrayList<Track> = storage.getHistory()
}