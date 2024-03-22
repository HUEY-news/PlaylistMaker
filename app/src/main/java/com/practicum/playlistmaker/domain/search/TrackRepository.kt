package com.practicum.playlistmaker.domain.search

interface TrackRepository {
    fun searchTrack(expression: String)
}