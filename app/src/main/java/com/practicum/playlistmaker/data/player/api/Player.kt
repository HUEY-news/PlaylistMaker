package com.practicum.playlistmaker.data.player.api

import com.practicum.playlistmaker.domain.player.model.PlayerState
import kotlinx.coroutines.flow.Flow

interface Player {

    fun getPlayerStateFlow(): Flow<PlayerState>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(url: String)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}