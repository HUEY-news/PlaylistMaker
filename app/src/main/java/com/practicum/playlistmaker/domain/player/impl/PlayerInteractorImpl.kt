package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.repository.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {

    override fun getPlayerState() = playerRepository.getPlayerState()
    override fun getPlayerCurrentPosition() = playerRepository.getPlayerCurrentPosition()
    override fun preparePlayer(url: String) = playerRepository.preparePlayer(url)
    override fun playbackControl() = playerRepository.playbackControl()
    override fun onPause() = playerRepository.onPause()
    override fun onDestroy() = playerRepository.onDestroy()
}