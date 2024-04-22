package com.practicum.playlistmaker.data.library

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.convertor.DbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.library.PlaylistRepository
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConvertor,
    private val gson: Gson
): PlaylistRepository {

    override suspend fun createNewPlaylist(name: String, description: String, cover: Uri?) {
        val playlistEntity = PlaylistEntity(
            playlistName = name,
            playlistDescription = description,
            playlistCoverUri = cover?.toString(),
            tracksIdentifiers = gson.toJson(emptyList<Int>()),
            numberOfTracks = 0
        )
        appDatabase.playlistDao().addItem(playlistEntity)
    }

    override suspend fun removePlaylistFromLibrary(playlist: Playlist) {
        val playlistEntity = dbConvertor.map(playlist)
        appDatabase.playlistDao().removeItem(playlistEntity)
    }

    override suspend fun updatePlaylist(track: Track, playlist: Playlist) {
        val idList = createIdListFromJson(playlist.tracksIdentifiers)
        idList.add(0, track.trackId)
        val idString = createJsonFromIdList(idList)
        val updatedPlaylist = playlist.copy(tracksIdentifiers = idString, numberOfTracks = idList.size)
        val playlistEntity = dbConvertor.map(updatedPlaylist)
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

    override fun checkIfPlaylistContainsTrack(track: Track, playlist: Playlist): Boolean {
        val idList = createIdListFromJson(playlist.tracksIdentifiers)
        val id = track.trackId
        return (idList.contains(id))
    }

    override suspend fun deleteAllPlaylistsFromLibrary() {
        appDatabase.playlistDao().deleteAllItems()
    }

    override suspend fun addTrackToSavedList(track: Track) {
        val trackEntity = dbConvertor.mapTrackToSaved(track)
        appDatabase.savedTrackDao().addItem(trackEntity)
    }

    private fun convertFromPlaylistEntity(playlistEntityList: List<PlaylistEntity>): List<Playlist> {
        return playlistEntityList.map { playlistEntity -> dbConvertor.map(playlistEntity) }
    }

    private fun createIdListFromJson(json: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, listType)
    }

    private fun createJsonFromIdList(idList: ArrayList<Int>): String {
        return gson.toJson(idList)
    }
}