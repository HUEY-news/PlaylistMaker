package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl(private val player: MediaPlayer): Player {
    private val flow = MutableStateFlow(STATE_DEFAULT)

    override fun getPlayerStateFlow(): Flow<Int> {
        return flow
    }

    override fun getPlayerCurrentPosition(): Int {
        return this.player.currentPosition
    }

    override fun preparePlayer(track: Track) {
        this.player.setDataSource(track.previewUrl)
        this.player.prepareAsync()
        this.player.setOnPreparedListener {
            flow.value = STATE_PREPARED
        }
        this.player.setOnCompletionListener {
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
        this.player.start()
        flow.value = STATE_PLAYING
    }

    private fun pausePlayer() {
        this.player.pause()
        flow.value = STATE_PAUSED
    }

    override fun onPause() {
        pausePlayer()
    }

    override fun onDestroy() {
        this.player.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}
