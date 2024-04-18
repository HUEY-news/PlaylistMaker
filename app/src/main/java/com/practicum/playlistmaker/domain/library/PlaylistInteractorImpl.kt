package com.practicum.playlistmaker.domain.library

import android.net.Uri
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
):PlaylistInteractor {

    override suspend fun createNewPlaylist(name: String, description: String, cover: Uri?) {
        repository.createNewPlaylist(name, description, cover)
    }

    override suspend fun removePlaylistFromLibrary(playlist: Playlist) {
        repository.removePlaylistFromLibrary(playlist)
    }

    override suspend fun updatePlaylistFromLibrary(playlist: Playlist) {
        repository.updatePlaylistFromLibrary(playlist)
    }

    override fun getPlaylistFromLibrary(id: Int): Flow<Playlist> {
        return repository.getPlaylistFromLibrary(id)
    }

    override fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>> {
        return repository.getAllPlaylistsFromLibrary()
    }

    override suspend fun deleteAllPlaylistsFromLibrary() {
        repository.deleteAllPlaylistsFromLibrary()
    }
}