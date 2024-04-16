package com.practicum.playlistmaker.domain.library

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylistToLibrary(playlist: Playlist)
    suspend fun removePlaylistFromLibrary(playlist: Playlist)
    suspend fun updatePlaylistFromLibrary(playlist: Playlist)
    fun getPlaylistFromLibrary(id: Int): Flow<Playlist>
    fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>>
    suspend fun deleteAllPlaylistsFromLibrary()
}