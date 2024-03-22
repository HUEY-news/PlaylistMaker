package com.practicum.playlistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    fun initPlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    fun getPlayerStateFlow(): Flow<PlayerState>
    fun getCurrentPlayerPosition(): String

    fun isPlaying(): Boolean
}
