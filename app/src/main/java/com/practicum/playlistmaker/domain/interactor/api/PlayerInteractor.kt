package com.practicum.playlistmaker.domain.interactor.api

import com.practicum.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    fun getPlayerState(): Flow<Int>
    fun getPlayerCurrentPosition(): Int

    fun preparePlayer(track: Track)
    fun playbackControl()

    fun onPause()
    fun onDestroy()
}
