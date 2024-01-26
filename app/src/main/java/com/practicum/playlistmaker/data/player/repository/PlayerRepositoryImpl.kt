package com.practicum.playlistmaker.data.player.repository

import com.practicum.playlistmaker.data.player.api.Player
import com.practicum.playlistmaker.domain.player.repository.PlayerRepository

class PlayerRepositoryImpl(
    private val player: Player,
): PlayerRepository {

    override fun getPlayerState() = player.getPlayerStateFlow()
    override fun getPlayerCurrentPosition() = player.getPlayerCurrentPosition()
    override fun playbackControl() { player.playbackControl() }

    override fun onPrepare(url: String) { player.onPrepare(url) }
    override fun onPause() { player.onPause() }
    override fun onReset() { player.onReset() }
    override fun onDestroy() { player.onDestroy() }
}