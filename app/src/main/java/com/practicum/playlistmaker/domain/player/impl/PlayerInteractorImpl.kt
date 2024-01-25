package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.domain.player.api.PlayerInteractor
import com.practicum.playlistmaker.domain.player.repository.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {

    override fun getPlayerState() = repository.getPlayerState()
    override fun getPlayerCurrentPosition() = repository.getPlayerCurrentPosition()
    override fun preparePlayer(url: String) = repository.preparePlayer(url)
    override fun playbackControl() = repository.playbackControl()
    override fun onPause() = repository.onPause()
    override fun onDestroy() = repository.onDestroy()
}