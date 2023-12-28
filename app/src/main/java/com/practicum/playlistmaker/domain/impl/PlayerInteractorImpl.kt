package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractorImpl(
    private val trackTimer: TextView
): PlayerInteractor {

    private val mediaPlayer = MediaPlayer()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerUpdateRunnable = updateTimer(trackTimer)
    private var playerState = STATE_DEFAULT

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            stopUpdater()
            trackTimer.text = ZERO_CONDITION
        }
    }

    override fun playbackControl(){
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler.post(timerUpdateRunnable)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    private fun updateTimer(trackTimer: TextView) : Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING){
                    trackTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(mediaPlayer.currentPosition)
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }
    }

    private fun stopUpdater() {
        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
    }

    override fun onPause() {
        pausePlayer()
        stopUpdater()
    }

    override fun onDestroy() {
        mediaPlayer.release()
        stopUpdater()
    }

    companion object {
        private const val ZERO_CONDITION = "00:00"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 500L
    }
}