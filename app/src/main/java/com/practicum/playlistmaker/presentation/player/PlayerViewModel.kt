package com.practicum.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerState
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val interactor: PlayerInteractor): ViewModel() {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private fun renderState(state: PlayerState) { playerState.postValue(state) }

    init {
        viewModelScope.launch{
            interactor.getPlayerStateFlow().collect { state ->
                when (state) {
                    is PlayerState.Default -> renderState(PlayerState.Default())
                    is PlayerState.Prepared -> renderState(PlayerState.Prepared())
                    is PlayerState.Paused -> renderState(PlayerState.Paused(getCurrentPlayerPosition()))
                    is PlayerState.Playing -> renderState(PlayerState.Playing(getCurrentPlayerPosition()))
                }
            }
        }
    }

    fun onPause() {
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    fun initPlayer(track: Track) {
        interactor.initPlayer(track.previewUrl)
    }

    private fun startPlayer() {
        interactor.startPlayer()
        renderState(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        interactor.pausePlayer()
        timerJob?.cancel()
        renderState(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    fun releasePlayer() {
        interactor.releasePlayer()
        renderState(PlayerState.Default())
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (interactor.isPlaying()) {
                delay(300L)
                renderState(PlayerState.Playing(getCurrentPlayerPosition()))
                if (interactor.isPlaying() == false) {
                    timerJob?.cancel()
                    renderState(PlayerState.Paused(getCurrentPlayerPosition()))
                }
            }
        }
    }

    private fun getCurrentPlayerPosition(): String = interactor.getCurrentPlayerPosition()
}