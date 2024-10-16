package com.practicum.playlistmaker.domain.library

import android.net.Uri
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistInteractorImpl @Inject constructor(
    private val repository: PlaylistRepository
):PlaylistInteractor {

    override suspend fun createNewPlaylist(name: String, description: String, cover: Uri?) { repository.createNewPlaylist(name, description, cover) }
    override suspend fun removePlaylistFromLibrary(playlistId: Int) { repository.removePlaylistFromLibrary(playlistId) }
    override suspend fun updatePlaylist(track: Track, playlist: Playlist) { repository.updatePlaylist(track, playlist) }
    override suspend fun updatePlaylist(id: Int, name: String, description: String, cover: Uri?) { repository.updatePlaylist(id, name, description, cover) }
    override suspend fun deleteAllPlaylistsFromLibrary() { repository.deleteAllPlaylistsFromLibrary() }
    override fun getPlaylistFromLibrary(id: Int): Flow<Playlist> { return repository.getPlaylistFromLibrary(id) }
    override fun getAllPlaylistsFromLibrary(): Flow<List<Playlist>> { return repository.getAllPlaylistsFromLibrary() }
    override fun checkIfPlaylistContainsTrack(track: Track, playlist: Playlist): Boolean { return repository.checkIfPlaylistContainsTrack(track, playlist) }

    override suspend fun addTrackToSavedList(track: Track) { repository.addTrackToSavedList(track) }
    override suspend fun removeTrackFromSavedList(track: Track) { repository.removeTrackFromSavedList(track) }
    override suspend fun removeTrackFromPlaylist(track: Track, playlistId: Int) { repository.removeTrackFromPlaylist(track, playlistId) }
    override fun getTracksFromPlaylist(idListString: String): Flow<List<Track>> { return repository.getTracksFromPlaylist(idListString) }

    override suspend fun sharePlaylist(playlistId: Int) { repository.sharePlaylist(playlistId) }
}
