package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.iTunesApiService
import com.practicum.playlistmaker.data.player.Player
import com.practicum.playlistmaker.data.player.PlayerImpl
import com.practicum.playlistmaker.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.repository.PlayerRepository

class Creator {

    fun provideApiService() = RetrofitNetworkClient.retrofit.create(iTunesApiService::class.java)

    fun providePlayer(): Player = PlayerImpl()

    fun providePlayerRepository(): PlayerRepository = PlayerRepositoryImpl(
        player = providePlayer())

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(
        playerRepository = providePlayerRepository()
    )
}