package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.domain.player.PlayerRepository

class PlayerRepositoryImpl(private val player: Player, ): PlayerRepository {

    override fun initPlayer(url: String) { player.initPlayer(url) }
    override fun startPlayer() { player.startPlayer() }
    override fun pausePlayer() { player.pausePlayer() }
    override fun releasePlayer() { player.releasePlayer() }

    override fun getPlayerStateFlow() = player.getPlayerStateFlow()
    override fun getCurrentPlayerPosition(): String = player.getCurrentPlayerPosition()

    override fun isPlaying(): Boolean = player.isPlaying()
}