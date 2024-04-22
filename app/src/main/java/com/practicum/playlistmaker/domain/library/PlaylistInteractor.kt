package com.practicum.playlistmaker.domain.library

import android.net.Uri
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createNewPlaylist(name: String, description: String, cover: Uri?)
    suspend fun removePlaylistFromLibrary(playlist: Playlist)
    suspend fun updatePlaylist(track: Track, playlist: Playlist)
    fun getPlaylistFromLibrary(id: Int): Flow<Playlist>
    fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>>
    suspend fun deleteAllPlaylistsFromLibrary()
    suspend fun addTrackToSavedList(track: Track)
    fun checkIfPlaylistContainsTrack(track: Track, playlist: Playlist): Boolean
}