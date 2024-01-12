package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.track.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    fun getPlayerState(): Flow<PlayerState>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(track: Track)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}
