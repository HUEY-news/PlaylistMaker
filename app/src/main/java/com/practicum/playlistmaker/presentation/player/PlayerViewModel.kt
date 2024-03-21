package com.practicum.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerStateEnum
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val interactor: PlayerInteractor
    ): ViewModel() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerUpdateRunnable = updateTimer()

    private var playerStateLiveData = MutableLiveData<PlayerStateSealedInterface>(PlayerStateSealedInterface.Default)
    fun getPlayerStateLivedata(): LiveData<PlayerStateSealedInterface> = playerStateLiveData

    init {
        Log.v("TEST", "PlayerViewModel - СОЗДАНА")
        viewModelScope.launch{
            interactor.getPlayerState().collect { state ->
                when (state) {
                    PlayerStateEnum.DEFAULT -> {
                        playerStateLiveData.postValue(PlayerStateSealedInterface.Default)
                    }
                    PlayerStateEnum.PREPARED -> {
                        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
                        playerStateLiveData.postValue(PlayerStateSealedInterface.Prepared)
                    }
                    PlayerStateEnum.PAUSED -> {
                        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
                        playerStateLiveData.postValue(PlayerStateSealedInterface.Paused)
                    }
                    PlayerStateEnum.PLAYING -> {
                        playerStateLiveData.postValue(
                            PlayerStateSealedInterface.Playing(
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
                if (playerStateLiveData.value is PlayerStateSealedInterface.Playing) {
                    playerStateLiveData.postValue(
                        PlayerStateSealedInterface.Playing(
                        convertCurrentTime(interactor.getPlayerCurrentPosition())))
                    mainThreadHandler.postDelayed(this, DELAY_MILLIS)
                }
            }
        }
    }

    companion object {
        private const val DELAY_MILLIS = 100L
    }
}