package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.player.Player
import com.practicum.playlistmaker.data.player.PlayerImpl
import com.practicum.playlistmaker.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.interactor.api.PlayerInteractor
import com.practicum.playlistmaker.domain.interactor.api.TrackInteractor
import com.practicum.playlistmaker.domain.interactor.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.interactor.impl.TrackInteractorImpl
import com.practicum.playlistmaker.domain.repository.PlayerRepository
import com.practicum.playlistmaker.domain.repository.TrackRepository

object Creator {

    fun provideTrackInteractor(): TrackInteractor = TrackInteractorImpl(getTrackRepository())
    private fun getTrackRepository(): TrackRepository = TrackRepositoryImpl(getNetworkClient())
    private fun getNetworkClient(): NetworkClient = RetrofitNetworkClient()


    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(playerRepository = getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImpl(player = getPlayer())
    private fun getPlayer(): Player = PlayerImpl()

}