package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.domain.player.PlayerState
import com.practicum.playlistmaker.domain.track.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerImpl(private val player: MediaPlayer): Player {
    private val flow = MutableStateFlow(PlayerState.DEFAULT)

    override fun getPlayerStateFlow(): Flow<PlayerState> {
        return flow
    }

    override fun getPlayerCurrentPosition(): Int {
        return this.player.currentPosition
    }

    override fun preparePlayer(track: Track) {
        this.player.setDataSource(track.previewUrl)
        this.player.prepareAsync()
        this.player.setOnPreparedListener {
            flow.value = PlayerState.PREPARED
        }
        this.player.setOnCompletionListener {
            flow.value = PlayerState.PREPARED
        }
    }

    override fun playbackControl(){
        when (flow.value) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PAUSED -> startPlayer()
            PlayerState.PREPARED -> startPlayer()
            PlayerState.DEFAULT -> Log.e(
                "LOG_ERROR", "Не отработала функция preparePlayer \n" +
                "И плеер остался в состоянии PlayerState.DEFAULT \n" +
                "А такая ситуация является исключительной для функции playbackControl")
        }
    }

    private fun startPlayer() {
        this.player.start()
        flow.value = PlayerState.PLAYING
    }

    private fun pausePlayer() {
        this.player.pause()
        flow.value = PlayerState.PAUSED
    }

    override fun onPause() {
        pausePlayer()
    }

    override fun onDestroy() {
        this.player.release()
    }
}
