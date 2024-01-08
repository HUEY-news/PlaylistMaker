package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface Player {

    fun getPlayerState(): Flow<Int>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(track: Track)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}