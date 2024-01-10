package com.practicum.playlistmaker.domain.interactor.impl

import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.interactor.api.PlayerInteractor
import com.practicum.playlistmaker.domain.repository.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun getPlayerState() = playerRepository.getPlayerState()
    override fun getPlayerCurrentPosition() = playerRepository.getPlayerCurrentPosition()
    override fun preparePlayer(track: Track) = playerRepository.preparePlayer(track)
    override fun playbackControl() = playerRepository.playbackControl()
    override fun onPause() = playerRepository.onPause()
    override fun onDestroy() = playerRepository.onDestroy()
}