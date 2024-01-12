package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.track.Track

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun getPlayerState() = playerRepository.getPlayerState()
    override fun getPlayerCurrentPosition() = playerRepository.getPlayerCurrentPosition()
    override fun preparePlayer(track: Track) = playerRepository.preparePlayer(track)
    override fun playbackControl() = playerRepository.playbackControl()
    override fun onPause() = playerRepository.onPause()
    override fun onDestroy() = playerRepository.onDestroy()
}