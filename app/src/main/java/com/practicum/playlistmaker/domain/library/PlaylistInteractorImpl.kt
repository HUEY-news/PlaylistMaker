package com.practicum.playlistmaker.domain.library

import android.net.Uri
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
):PlaylistInteractor {

    override suspend fun createNewPlaylist(name: String, description: String, cover: Uri?) { repository.createNewPlaylist(name, description, cover) }
    override suspend fun removePlaylistFromLibrary(playlist: Playlist) { repository.removePlaylistFromLibrary(playlist) }
    override suspend fun updatePlaylist(track: Track, playlist: Playlist) { repository.updatePlaylist(track, playlist) }
    override fun getPlaylistFromLibrary(id: Int): Flow<Playlist> { return repository.getPlaylistFromLibrary(id) }
    override fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>> { return repository.getAllPlaylistsFromLibrary() }
    override suspend fun deleteAllPlaylistsFromLibrary() { repository.deleteAllPlaylistsFromLibrary() }
    override suspend fun addTrackToSavedList(track: Track) { repository.addTrackToSavedList(track) }
    override fun checkIfPlaylistContainsTrack(track: Track, playlist: Playlist): Boolean { return repository.checkIfPlaylistContainsTrack(track, playlist) }
}