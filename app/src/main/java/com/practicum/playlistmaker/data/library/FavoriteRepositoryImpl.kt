package com.practicum.playlistmaker.data.library

import com.practicum.playlistmaker.convertor.DbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.TrackEntity
import com.practicum.playlistmaker.domain.library.FavoriteRepository
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConvertor
) : FavoriteRepository {

    override suspend fun addTrackToFavoriteList(track: Track) {
        val trackEntity = dbConvertor.map(track)
        appDatabase.trackDao().addTrackToFavoriteList(trackEntity)
    }

    override suspend fun removeTrackFromFavoriteList(track: Track) {
        val trackEntity = dbConvertor.map(track)
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
        return trackEntityList.map { trackEntity -> dbConvertor.map(trackEntity) }
    }
}