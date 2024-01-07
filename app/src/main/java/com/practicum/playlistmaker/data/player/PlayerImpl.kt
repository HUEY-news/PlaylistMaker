package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl: Player {

    private val mediaPlayer = MediaPlayer()
    private val flow = MutableStateFlow(STATE_DEFAULT)

    override fun getPlayerState(): Flow<Int> {
        return flow
    }

    override fun getPlayerCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            flow.value = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            flow.value = STATE_PREPARED
        }
    }

    override fun playbackControl(){
        when (flow.value) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        flow.value = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        flow.value = STATE_PAUSED
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
