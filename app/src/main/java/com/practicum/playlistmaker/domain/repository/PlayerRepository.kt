package com.practicum.playlistmaker.domain.repository

import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getPlayerState(): Flow<Int>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(track: Track)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}