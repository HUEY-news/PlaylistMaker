package com.practicum.playlistmaker.presentation.library

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.library.PlaylistInteractor
import kotlinx.coroutines.launch

class LibraryPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val currentState = MutableLiveData<PlaylistState>()
    fun observeCurrentState(): LiveData<PlaylistState> = currentState
    private fun renderState(state: PlaylistState) { currentState.postValue(state) }

    init { updateCurrentState() }

    fun onResume() { updateCurrentState() }

    private fun updateCurrentState() {
        viewModelScope.launch {
            playlistInteractor
                .getAllPlaylistsFromLibrary()
                .collect { data ->
                    if (data.isEmpty()) {
                        renderState(PlaylistState.Empty)
                        Log.e("TEST", "data is empty")
                    }
                    else {
                        renderState(PlaylistState.Content(data))
                        Log.i("TEST", "data = $data")
                    }
                }
        }
    }
}