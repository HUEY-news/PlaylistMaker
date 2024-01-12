package com.practicum.playlistmaker.domain.track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>?
}