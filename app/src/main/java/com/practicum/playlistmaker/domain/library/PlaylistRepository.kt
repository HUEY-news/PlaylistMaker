package com.practicum.playlistmaker.domain.library

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createNewPlaylist(name: String, description: String, cover: Uri?)
    suspend fun removePlaylistFromLibrary(playlist: Playlist)
    suspend fun updatePlaylistFromLibrary(playlist: Playlist)
    fun getPlaylistFromLibrary(id: Int): Flow<Playlist>
    fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>>
    suspend fun deleteAllPlaylistsFromLibrary()
}