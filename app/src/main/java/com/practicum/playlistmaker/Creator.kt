package com.practicum.playlistmaker

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.player.Player
import com.practicum.playlistmaker.data.player.PlayerImpl
import com.practicum.playlistmaker.data.player.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.track.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.domain.track.TrackInteractorImpl
import com.practicum.playlistmaker.domain.track.TrackRepository

object Creator {

    fun provideTrackInteractor(): TrackInteractor = TrackInteractorImpl(getTrackRepository())
    private fun getTrackRepository(): TrackRepository = TrackRepositoryImpl(getNetworkClient())
    private fun getNetworkClient(): NetworkClient = RetrofitNetworkClient()


    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(playerRepository = getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImpl(player = getPlayer())
    private fun getPlayer(): Player = PlayerImpl(MediaPlayer())

}