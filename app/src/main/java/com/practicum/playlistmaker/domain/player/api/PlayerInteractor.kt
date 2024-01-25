package com.practicum.playlistmaker.domain.player.api

import com.practicum.playlistmaker.domain.player.model.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    fun getPlayerState(): Flow<PlayerState>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(url: String)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}
