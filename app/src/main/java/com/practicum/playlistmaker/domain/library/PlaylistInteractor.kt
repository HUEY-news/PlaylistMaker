package com.practicum.playlistmaker.domain.library

import android.net.Uri
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun createNewPlaylist(name: String, description: String, cover: Uri?)
    suspend fun removePlaylistFromLibrary(playlistId: Int)
    suspend fun updatePlaylist(track: Track, playlist: Playlist)
    suspend fun deleteAllPlaylistsFromLibrary()
    fun getPlaylistFromLibrary(id: Int): Flow<Playlist>
    fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>>
    fun checkIfPlaylistContainsTrack(track: Track, playlist: Playlist): Boolean

    suspend fun addTrackToSavedList(track: Track)
    suspend fun removeTrackFromSavedList(track: Track)
    suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int)
    fun getTracksFromPlaylist(idListString: String): Flow<List<Track>>

    suspend fun sharePlaylist(playlistId: Int)
}