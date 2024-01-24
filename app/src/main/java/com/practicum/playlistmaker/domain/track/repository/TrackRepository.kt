package com.practicum.playlistmaker.domain.track.repository

import com.practicum.playlistmaker.domain.track.model.Track
import com.practicum.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}