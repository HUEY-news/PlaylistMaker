package com.practicum.playlistmaker

import android.media.MediaPlayer
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


    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(playerRepository = getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImpl(player = getPlayer())
    private fun getPlayer(): Player = PlayerImpl(player = MediaPlayer())
}
