package com.practicum.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.favorite.FavoriteInteractor
import kotlinx.coroutines.launch

class LibraryFavoriteViewModel(
    private val favoriteInteractor: FavoriteInteractor
): ViewModel() {

    private val currentState = MutableLiveData<FavoriteState>()
    fun observeCurrentState(): LiveData<FavoriteState> = currentState
    private fun renderState(state: FavoriteState) { currentState.postValue(state) }

    init { updateCurrentState() }

    fun onResume() { updateCurrentState() }

    private fun updateCurrentState() {
        viewModelScope.launch {
            favoriteInteractor
                .getFavoriteTrackList()
                .collect { trackList ->
                    if (trackList.isEmpty()) renderState(FavoriteState.Empty)
                    else renderState(FavoriteState.Content(trackList))
                }
        }
    }
}