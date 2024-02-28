package com.practicum.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.model.PlayerState
import com.practicum.playlistmaker.domain.track.model.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val interactor: PlayerInteractor
    ): ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerUpdateRunnable = updateTimer()

    private var playerStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Default)
    fun getPlayerStateLivedata(): LiveData<PlayerScreenState> = playerStateLiveData

    init {
        Log.v("TEST", "PlayerViewModel - СОЗДАНА")
        viewModelScope.launch{
            interactor.getPlayerState().collect { state ->
                when (state) {
                    PlayerState.DEFAULT -> {
                        playerStateLiveData.postValue(PlayerScreenState.Default)
                    }
                    PlayerState.PREPARED -> {
                        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
                        playerStateLiveData.postValue(PlayerScreenState.Prepared)
                    }
                    PlayerState.PAUSED -> {
                        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
                        playerStateLiveData.postValue(PlayerScreenState.Paused)
                    }
                    PlayerState.PLAYING -> {
                        playerStateLiveData.postValue(
                            PlayerScreenState.Playing(
                        convertCurrentTime(interactor.getPlayerCurrentPosition())))
                    }
                }
            }
        }
    }

    fun playbackControl() { interactor.playbackControl() }
    fun startUpdater() { mainThreadHandler.post(timerUpdateRunnable) }
    fun stopUpdater() { mainThreadHandler.removeCallbacks(timerUpdateRunnable) }

    fun onPrepare(track: Track) {
        interactor.onPrepare(track.previewUrl)
    }

    fun onPause() {
        interactor.onPause()
        stopUpdater()
    }

    fun onReset() {
        interactor.onReset()
        stopUpdater()
    }

    fun onDestroy() {
        interactor.onDestroy()
        stopUpdater()
    }

    override fun onCleared() {
        Log.v("TEST", "PlayerViewModel - ОЧИЩЕНА")
    }

    private fun convertCurrentTime(time: Int): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

    private fun updateTimer() : Runnable {
        return object : Runnable {
            override fun run() {
                if (playerStateLiveData.value is PlayerScreenState.Playing) {
                    playerStateLiveData.postValue(
                        PlayerScreenState.Playing(
                        convertCurrentTime(interactor.getPlayerCurrentPosition())))
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }
    }

    companion object {
        private const val DELAY = 100L
    }
}