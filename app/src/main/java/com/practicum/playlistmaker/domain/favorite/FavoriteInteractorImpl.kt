package com.practicum.playlistmaker.domain.favorite

import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val repository: FavoriteRepository
): FavoriteInteractor {

    override suspend fun addTrackToFavoriteList(track: Track) {
        repository.addTrackToFavoriteList(track)
    }

    override suspend fun removeTrackFromFavoriteList(track: Track) {
        repository.removeTrackFromFavoriteList(track)
    }

    override fun getFavoriteTrackList(): Flow<List<Track>> {
        return repository.getFavoriteTrackList()
    }
}