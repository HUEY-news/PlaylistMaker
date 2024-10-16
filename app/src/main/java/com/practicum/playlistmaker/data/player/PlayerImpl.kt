package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class PlayerImpl @Inject constructor(
    private val mediaPlayer: MediaPlayer
): Player {

    private val flow = MutableStateFlow<PlayerState>(PlayerState.Default())
    override fun getPlayerStateFlow(): Flow<PlayerState> = flow

    override fun initPlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()

        mediaPlayer.setOnPreparedListener {
            flow.value = PlayerState.Prepared()
            mediaPlayer.seekTo(0)
        }

        mediaPlayer.setOnCompletionListener {
            flow.value = PlayerState.Prepared()
            mediaPlayer.seekTo(0)}
    }

    override fun startPlayer() {
        mediaPlayer.start()
        flow.value = PlayerState.Playing(getCurrentPlayerPosition())
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        flow.value = PlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun releasePlayer() {
        mediaPlayer.reset()
        flow.value = PlayerState.Default()
    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    override fun getCurrentPlayerPosition(): String = dateFormat.format(mediaPlayer.currentPosition) ?: "00:00"

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying
}
