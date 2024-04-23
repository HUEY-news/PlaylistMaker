package com.practicum.playlistmaker.presentation.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.library.PlaylistInteractor
import kotlinx.coroutines.launch

class LibraryNewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    fun createNewPlaylist(name: String, description: String, cover: Uri?) {
        viewModelScope.launch { playlistInteractor.createNewPlaylist(name, description, cover) }
    }
}