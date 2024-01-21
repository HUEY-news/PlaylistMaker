package com.practicum.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerState
import com.practicum.playlistmaker.domain.track.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerPresenter(
    private val view: PlayerView,
    private val lifecycleScope: LifecycleCoroutineScope
    ) {
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerUpdateRunnable = updateTimer()
    private var state = PlayerState.DEFAULT
    private val player: PlayerInteractor = Creator.providePlayerInteractor()

    fun onCreate() {
        lifecycleScope.launch(Dispatchers.IO) {
            player.getPlayerState().collect { state ->
                this@PlayerPresenter.state = state
                mainThreadHandler.post { checkPlayerState(state) }
            }
        }
    }

    fun onPause() {
        player.onPause()
        stopUpdater()
    }

    fun onDestroy() {
        player.onDestroy()
        stopUpdater()
    }

    fun preparePlayer(track: Track?) {
        if (track != null) player.preparePlayer(track)
        else Log.e("HOUSTON_LOG_ERROR", "Вместо объекта класса Track из интента получен null")
    }

    fun playbackControl() {
        player.playbackControl()
    }

    private fun checkPlayerState(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> {
                view.setPauseImage()
                mainThreadHandler.post(timerUpdateRunnable)
            }
            PlayerState.PREPARED -> {
                view.setPlayImage()
                stopUpdater()
                view.resetTimer()
            }
            PlayerState.PAUSED -> {
                view.setPlayImage()
            }
            else -> {}
        }
    }

    private fun stopUpdater() {
        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
    }
    private fun updateTimer() : Runnable {
        return object : Runnable {
            override fun run() {
                if (state == PlayerState.PLAYING){
                    view.setTimer(SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(player.getPlayerCurrentPosition()))
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }
    }
    companion object {
        private const val DELAY = 500L
    }
}