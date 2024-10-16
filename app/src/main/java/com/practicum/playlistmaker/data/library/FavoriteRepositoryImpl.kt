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
    private val database: AppDatabase,
    private val convertor: DbConvertor
) : FavoriteRepository {

    override suspend fun addTrackToFavoriteList(track: Track) {
        val trackEntity = convertor.mapTrackToFavorite(track)
        database.favoriteTrackDao().addTrackToFavoriteList(trackEntity)
    }

    override suspend fun removeTrackFromFavoriteList(track: Track) {
        val trackEntity = convertor.mapTrackToFavorite(track)
        database.favoriteTrackDao().removeTrackFromFavoriteList(trackEntity)
    }

    override fun getFavoriteTrackList(): Flow<List<Track>> = flow {
        val trackEntityList = database.favoriteTrackDao().getFavoriteTrackList()
        val trackList = convertFromTrackEntity(trackEntityList.sortedByDescending { it.addingTime })
        val idList = database.favoriteTrackDao().getFavoriteIdList()
        for (track in trackList) track.isFavorite = idList.contains(track.trackId)
        emit(trackList)
    }

    private fun convertFromTrackEntity(trackEntityList: List<FavoriteTrackEntity>): List<Track> {
        return trackEntityList.map { trackEntity -> convertor.mapFavoriteToTrack(trackEntity) }
    }
}
