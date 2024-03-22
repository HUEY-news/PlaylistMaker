package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.flow.Flow

interface Player {

    fun initPlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    fun getPlayerStateFlow(): Flow<PlayerState>
    fun getCurrentPlayerPosition(): String

    fun isPlaying(): Boolean
}