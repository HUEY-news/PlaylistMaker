package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl(
    private val player: MediaPlayer
): Player {

    private val flow = MutableStateFlow(PlayerState.DEFAULT)

    override fun playbackControl(){
        when (flow.value) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PAUSED -> startPlayer()
            PlayerState.PREPARED -> startPlayer()
            PlayerState.DEFAULT -> {}
        }
    }

    private fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener { flow.value = PlayerState.PREPARED }
        player.setOnCompletionListener { flow.value = PlayerState.PREPARED }
    }

    private fun startPlayer() {
        player.start()
        flow.value = PlayerState.PLAYING
    }

    private fun pausePlayer() {
        player.pause()
        flow.value = PlayerState.PAUSED
    }

    private fun resetPlayer() {
        player.reset()
        flow.value = PlayerState.DEFAULT
    }

    private fun destroyPlayer() {
        player.release()
    }

    override fun getPlayerStateFlow(): Flow<PlayerState> = flow
    override fun getPlayerCurrentPosition(): Int = player.currentPosition
    override fun onPrepare(url: String) { preparePlayer(url) }
    override fun onPause() { pausePlayer() }
    override fun onReset() { resetPlayer() }
    override fun onDestroy() { destroyPlayer() }
}
