package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.player.Player
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val player: Player,
): PlayerRepository {

    override fun getPlayerState() = player.getPlayerState()
    override fun getPlayerCurrentPosition() = player.getPlayerCurrentPosition()
    override fun preparePlayer(track: Track) = player.preparePlayer(track)
    override fun playbackControl() = player.playbackControl()
    override fun onPause() = player.onPause()
    override fun onDestroy() = player.onDestroy()
}