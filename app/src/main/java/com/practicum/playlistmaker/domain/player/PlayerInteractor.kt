package com.practicum.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    fun getPlayerState(): Flow<PlayerState>
    fun getPlayerCurrentPosition(): Int
    fun playbackControl()

    fun onPrepare(url: String)
    fun onPause()
    fun onReset()
    fun onDestroy()
}
