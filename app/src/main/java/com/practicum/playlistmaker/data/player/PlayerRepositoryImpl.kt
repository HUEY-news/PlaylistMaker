package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.track.Track

class PlayerRepositoryImpl(
    private val player: Player,
): PlayerRepository {

    override fun getPlayerState() = player.getPlayerStateFlow()
    override fun getPlayerCurrentPosition() = player.getPlayerCurrentPosition()
    override fun preparePlayer(track: Track) = player.preparePlayer(track)
    override fun playbackControl() = player.playbackControl()
    override fun onPause() = player.onPause()
    override fun onDestroy() = player.onDestroy()
}