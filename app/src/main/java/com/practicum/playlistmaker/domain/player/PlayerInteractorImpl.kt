package com.practicum.playlistmaker.domain.player

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {

    override fun initPlayer(url: String) { repository.initPlayer(url) }
    override fun startPlayer() { repository.startPlayer() }
    override fun pausePlayer() { repository.pausePlayer() }
    override fun releasePlayer() { repository.releasePlayer() }

    override fun getPlayerStateFlow() = repository.getPlayerStateFlow()
    override fun getCurrentPlayerPosition(): String = repository.getCurrentPlayerPosition()

    override fun isPlaying(): Boolean = repository.isPlaying()
}