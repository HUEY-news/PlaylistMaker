package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.player.PlayerState
import kotlinx.coroutines.flow.Flow

interface Player {

    fun getPlayerStateFlow(): Flow<PlayerState>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(track: Track)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}