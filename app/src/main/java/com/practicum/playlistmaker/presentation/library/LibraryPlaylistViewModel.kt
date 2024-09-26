package com.practicum.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.library.PlaylistInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val currentState = MutableLiveData<PlaylistPageState>()
    fun observeCurrentState(): LiveData<PlaylistPageState> = currentState
    private fun renderState(state: PlaylistPageState) { currentState.postValue(state) }

    init { updateCurrentState() }

    fun onResume() { updateCurrentState() }

    private fun updateCurrentState() {
        viewModelScope.launch {
            playlistInteractor
                .getAllPlaylistsFromLibrary()
                .collect { data ->
                    if (data.isEmpty()) renderState(PlaylistPageState.Empty)
                    else renderState(PlaylistPageState.Content(data))
                }
        }
    }
}