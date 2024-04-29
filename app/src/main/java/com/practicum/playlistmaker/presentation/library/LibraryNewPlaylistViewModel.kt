package com.practicum.playlistmaker.presentation.library

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.library.PlaylistInteractor
import kotlinx.coroutines.launch

class LibraryNewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val currentPlaylist = MutableLiveData<Playlist>()
    fun observeCurrentPlaylist(): LiveData<Playlist> = currentPlaylist

    fun createNewPlaylist(name: String, description: String, cover: Uri?) {
        viewModelScope.launch {
            playlistInteractor.createNewPlaylist(name, description, cover)
        }
    }

    fun getPlaylistFromLibrary(playlistId: Int?) {
        playlistId?.let {
            viewModelScope.launch {
                playlistInteractor
                    .getPlaylistFromLibrary(playlistId)
                    .collect { playlist ->
                        currentPlaylist.postValue(playlist)
                    }
            }
        }
    }

    fun updatePlaylist(id: Int, name: String, description: String, cover: Uri?) {
        viewModelScope.launch {
            playlistInteractor.updatePlaylist(id, name, description, cover)
        }
    }
}