package com.practicum.playlistmaker.test

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class TestPlayerViewModel: ViewModel() {
    private val url: String = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/ac/c7/d1/acc7d13f-6634-495f-caf6-491eccb505e8/mzaf_4002676889906514534.plus.aac.p.m4a"
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var timerJob: Job? = null

    private val playerState = MutableLiveData<TestPlayerState>(TestPlayerState.Default())
    fun observePlayerState(): LiveData<TestPlayerState> = playerState

    init { initMediaPlayer() }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    fun onPlayButtonClicked() {
        when (playerState.value) {
            is TestPlayerState.Playing -> pausePlayer()
            is TestPlayerState.Prepared, is TestPlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    private fun initMediaPlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { playerState.postValue(TestPlayerState.Prepared()) }
        mediaPlayer.setOnCompletionListener { playerState.postValue(TestPlayerState.Prepared()) }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(TestPlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(TestPlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.postValue(TestPlayerState.Default())
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                playerState.postValue(TestPlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(mediaPlayer.currentPosition) ?: "00:00"
    }
}