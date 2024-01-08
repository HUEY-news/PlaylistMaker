package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.entity.Track

interface TrackRepository {
    fun searchTrack(expression: String): List<Track>
}