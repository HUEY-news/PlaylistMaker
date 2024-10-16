package com.practicum.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.library.FavoriteInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryFavoriteViewModel @Inject constructor(
    private val favoriteInteractor: FavoriteInteractor
): ViewModel() {

    private val currentState = MutableLiveData<FavoritePageState>()
    fun observeCurrentState(): LiveData<FavoritePageState> = currentState
    private fun renderState(state: FavoritePageState) { currentState.postValue(state) }

    init { updateCurrentState() }

    fun onResume() { updateCurrentState() }

    private fun updateCurrentState() {
        viewModelScope.launch {
            favoriteInteractor
                .getFavoriteTrackList()
                .collect { data ->
                    if (data.isEmpty()) renderState(FavoritePageState.Empty)
                    else renderState(FavoritePageState.Content(data))
                }
        }
    }
}