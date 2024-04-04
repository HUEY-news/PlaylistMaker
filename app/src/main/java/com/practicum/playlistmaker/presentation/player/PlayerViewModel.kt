package com.practicum.playlistmaker.presentation.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.favorite.FavoriteInteractor
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerState
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private var timerJob: Job? = null
    private lateinit var favouriteTrackList: List<Track>

    private val currentTrack = MutableLiveData<Track>()
    fun observeCurrentTrack(): LiveData<Track> = currentTrack

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private fun renderState(state: PlayerState) { playerState.postValue(state) }

    private val isFavorite = MutableLiveData<Boolean>()
    fun observeFavorite(): LiveData<Boolean> = isFavorite

    init {
        viewModelScope.launch {
            playerInteractor.getPlayerStateFlow().collect { state ->
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

    fun onFavoriteClicked() {
        viewModelScope.launch {
            currentTrack.value?.let { track ->
                if (track.isFavorite) {
                    favoriteInteractor.removeTrackFromFavoriteList(track)
                    track.isFavorite = false
                    isFavorite.postValue(false)
                    Log.e("TEST", "isFavorite = ${track.isFavorite}")
                } else {
                    favoriteInteractor.addTrackToFavoriteList(track)
                    track.isFavorite = true
                    isFavorite.postValue(true)
                    Log.i("TEST", "isFavorite = ${track.isFavorite}")
                }
            }
        }
    }

    fun initPlayer(track: Track?) {
        track?.let { notNullTrack ->
            currentTrack.postValue(notNullTrack)
            playerInteractor.initPlayer(notNullTrack.previewUrl)
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        timerJob?.cancel()
        renderState(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        renderState(PlayerState.Default())
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(300L)
                renderState(PlayerState.Playing(getCurrentPlayerPosition()))
                if (playerInteractor.isPlaying() == false) {
                    timerJob?.cancel()
                    renderState(PlayerState.Paused(getCurrentPlayerPosition()))
                }
            }
        }
    }

    private fun getCurrentPlayerPosition(): String = playerInteractor.getCurrentPlayerPosition()
}