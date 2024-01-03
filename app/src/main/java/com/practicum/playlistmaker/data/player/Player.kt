package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface Player {

    fun getPlayerState(): Flow<Int>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(track: TrackDto)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}