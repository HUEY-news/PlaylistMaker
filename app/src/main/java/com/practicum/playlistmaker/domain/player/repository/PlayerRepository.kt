package com.practicum.playlistmaker.domain.player.repository

import com.practicum.playlistmaker.domain.player.model.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getPlayerState(): Flow<PlayerState>
    fun getPlayerCurrentPosition(): Int
    fun playbackControl()

    fun onPrepare(url: String)
    fun onPause()
    fun onReset()
    fun onDestroy()
}