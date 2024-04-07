package com.practicum.playlistmaker.data.favorite

import com.practicum.playlistmaker.convertor.TrackDbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.TrackEntity
import com.practicum.playlistmaker.domain.favorite.FavoriteRepository
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteRepository {

    override suspend fun addTrackToFavoriteList(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.trackDao().addTrackToFavoriteList(trackEntity)
    }

    override suspend fun removeTrackFromFavoriteList(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.trackDao().removeTrackFromFavoriteList(trackEntity)
    }

    override fun getFavoriteTrackList(): Flow<List<Track>> = flow {
        val trackEntityList = appDatabase.trackDao().getFavoriteTrackList()
        val trackList = convertFromTrackEntity(trackEntityList.sortedByDescending { it.addingTime })
        val idList = appDatabase.trackDao().getFavoriteIdList()
        for (track in trackList) track.isFavorite = idList.contains(track.trackId)
        emit(trackList)
    }

    private fun convertFromTrackEntity(trackEntityList: List<TrackEntity>): List<Track> {
        return trackEntityList.map { trackEntity -> trackDbConvertor.map(trackEntity) }
    }
}