package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.flow.Flow

interface Player {

    fun getPlayerStateFlow(): Flow<PlayerState>
    fun getPlayerCurrentPosition(): Int
    fun playbackControl()

    fun onPrepare(url: String)
    fun onPause()
    fun onReset()
    fun onDestroy()
}