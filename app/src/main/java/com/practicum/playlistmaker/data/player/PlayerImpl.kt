package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.PlayerStateEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl(
    private val player: MediaPlayer
): Player {

    private val flow = MutableStateFlow(PlayerStateEnum.DEFAULT)

    override fun playbackControl(){
        when (flow.value) {
            PlayerStateEnum.PLAYING -> pausePlayer()
            PlayerStateEnum.PAUSED -> startPlayer()
            PlayerStateEnum.PREPARED -> startPlayer()
            PlayerStateEnum.DEFAULT -> {}
        }
    }

    private fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener { flow.value = PlayerStateEnum.PREPARED }
        player.setOnCompletionListener { flow.value = PlayerStateEnum.PREPARED }
    }

    private fun startPlayer() {
        player.start()
        flow.value = PlayerStateEnum.PLAYING
    }

    private fun pausePlayer() {
        player.pause()
        flow.value = PlayerStateEnum.PAUSED
    }

    private fun resetPlayer() {
        player.reset()
        flow.value = PlayerStateEnum.DEFAULT
    }

    private fun destroyPlayer() {
        player.release()
    }

    override fun getPlayerStateFlow(): Flow<PlayerStateEnum> = flow
    override fun getPlayerCurrentPosition(): Int = player.currentPosition
    override fun onPrepare(url: String) { preparePlayer(url) }
    override fun onPause() { pausePlayer() }
    override fun onReset() { resetPlayer() }
    override fun onDestroy() { destroyPlayer() }
}
