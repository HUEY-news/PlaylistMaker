package com.practicum.playlistmaker.domain.search.repository

import com.practicum.playlistmaker.domain.track.model.Track

interface SearchRepository {
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}