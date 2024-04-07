package com.practicum.playlistmaker.domain.favorite

import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun addTrackToFavoriteList(track: Track)
    suspend fun removeTrackFromFavoriteList(track: Track)
    fun getFavoriteTrackList(): Flow<List<Track>>
}