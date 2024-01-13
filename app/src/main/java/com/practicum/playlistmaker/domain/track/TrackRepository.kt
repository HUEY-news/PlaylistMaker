package com.practicum.playlistmaker.domain.track

import com.practicum.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}