package com.practicum.playlistmaker.data.library

import android.net.Uri
import com.google.gson.Gson
import com.practicum.playlistmaker.convertor.DbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.library.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConvertor
): PlaylistRepository {

    override suspend fun createNewPlaylist(name: String, description: String, cover: Uri?) {
        val playlistEntity = PlaylistEntity(
            playlistName = name,
            playlistDescription = description,
            playlistCoverUri = cover?.toString(),
            tracksIdentifiers = Gson().toJson(emptyList<Int>()),
            numberOfTracks = 0
        )
        appDatabase.playlistDao().addItem(playlistEntity)
    }

    override suspend fun removePlaylistFromLibrary(playlist: Playlist) {
        val playlistEntity = dbConvertor.map(playlist)
        appDatabase.playlistDao().removeItem(playlistEntity)
    }

    override suspend fun updatePlaylistFromLibrary(playlist: Playlist) {
        val playlistEntity = dbConvertor.map(playlist)
        appDatabase.playlistDao().updateItem(playlistEntity)
    }

    override fun getPlaylistFromLibrary(id: Int): Flow<Playlist> = flow {
        val playlistEntity = appDatabase.playlistDao().getItem(id)
        val playlist = dbConvertor.map(playlistEntity)
        emit(playlist)
    }

    override fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>> = flow {
        val playlistEntityList = appDatabase.playlistDao().getAllItems()
        val playlistList = convertFromPlaylistEntity(playlistEntityList.sortedByDescending { it.playlistId })
        emit(playlistList)
    }

    override suspend fun deleteAllPlaylistsFromLibrary() {
        appDatabase.playlistDao().deleteAllItems()
    }

    private fun convertFromPlaylistEntity(playlistEntityList: List<PlaylistEntity>): List<Playlist> {
        return playlistEntityList.map { playlistEntity -> dbConvertor.map(playlistEntity) }
    }
}