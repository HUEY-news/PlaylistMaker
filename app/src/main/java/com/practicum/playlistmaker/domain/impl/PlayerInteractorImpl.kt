package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track

class PlayerInteractorImpl: PlayerInteractor {

    private val mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getPlayerCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
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
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        pausePlayer()
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}
