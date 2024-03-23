package com.practicum.playlistmaker.domain.search

import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>>
}