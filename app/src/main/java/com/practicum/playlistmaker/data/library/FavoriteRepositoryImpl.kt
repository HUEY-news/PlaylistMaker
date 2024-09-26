package com.practicum.playlistmaker.data.library

import com.practicum.playlistmaker.convertor.DbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.domain.library.FavoriteRepository
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConvertor
) : FavoriteRepository {

    override suspend fun addTrackToFavoriteList(track: Track) {
        val trackEntity = dbConvertor.mapTrackToFavorite(track)
        appDatabase.favoriteTrackDao().addTrackToFavoriteList(trackEntity)
    }

    override suspend fun removeTrackFromFavoriteList(track: Track) {
        val trackEntity = dbConvertor.mapTrackToFavorite(track)
        appDatabase.favoriteTrackDao().removeTrackFromFavoriteList(trackEntity)
    }

    override fun getFavoriteTrackList(): Flow<List<Track>> = flow {
        val trackEntityList = appDatabase.favoriteTrackDao().getFavoriteTrackList()
        val trackList = convertFromTrackEntity(trackEntityList.sortedByDescending { it.addingTime })
        val idList = appDatabase.favoriteTrackDao().getFavoriteIdList()
        for (track in trackList) track.isFavorite = idList.contains(track.trackId)
        emit(trackList)
    }

    private fun convertFromTrackEntity(trackEntityList: List<FavoriteTrackEntity>): List<Track> {
        return trackEntityList.map { trackEntity -> dbConvertor.mapFavoriteToTrack(trackEntity) }
    }
}
