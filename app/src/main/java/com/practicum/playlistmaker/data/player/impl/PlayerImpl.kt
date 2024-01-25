package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.api.Player
import com.practicum.playlistmaker.domain.player.model.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl(
    private val player: MediaPlayer
): Player {

    private val flow = MutableStateFlow(PlayerState.DEFAULT)
    override fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener { flow.value = PlayerState.PREPARED }
        player.setOnCompletionListener { flow.value = PlayerState.PREPARED }
    }

    override fun playbackControl(){
        when (flow.value) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PAUSED -> startPlayer()
            PlayerState.PREPARED -> startPlayer()
            PlayerState.DEFAULT -> {}
        }
    }
    private fun startPlayer() {
        player.start()
        flow.value = PlayerState.PLAYING
    }
    private fun pausePlayer() {
        player.pause()
        flow.value = PlayerState.PAUSED
    }

    override fun getPlayerStateFlow(): Flow<PlayerState> = flow
    override fun getPlayerCurrentPosition(): Int = player.currentPosition
    override fun onPause() = pausePlayer()
    override fun onDestroy() = player.release()
}
