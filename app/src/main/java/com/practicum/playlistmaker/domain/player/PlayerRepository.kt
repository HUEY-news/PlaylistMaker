package com.practicum.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getPlayerState(): Flow<PlayerStateEnum>
    fun getPlayerCurrentPosition(): Int
    fun playbackControl()

    fun onPrepare(url: String)
    fun onPause()
    fun onReset()
    fun onDestroy()
}