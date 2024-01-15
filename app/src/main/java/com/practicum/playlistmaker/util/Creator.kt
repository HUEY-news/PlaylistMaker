package com.practicum.playlistmaker.util

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.LifecycleCoroutineScope
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
import com.practicum.playlistmaker.presentation.player.PlayerController
import com.practicum.playlistmaker.presentation.search.SearchPresenter
import com.practicum.playlistmaker.presentation.search.SearchView

object Creator {

    fun provideSearchPresenter(
        searchView: SearchView,
        context: Context) =
        SearchPresenter(searchView, context)

    fun providePlayerController(
        activity: Activity,
        lifecycleScope: LifecycleCoroutineScope) =
        PlayerController(activity, lifecycleScope)

    fun provideTrackInteractor(context: Context): TrackInteractor = TrackInteractorImpl(getTrackRepository(context))
    private fun getTrackRepository(context: Context): TrackRepository = TrackRepositoryImpl(getNetworkClient(context))
    private fun getNetworkClient(context: Context): NetworkClient = RetrofitNetworkClient(context)

    fun providePlayerInteractor(): PlayerInteractor = PlayerInteractorImpl(playerRepository = getPlayerRepository())
    private fun getPlayerRepository(): PlayerRepository = PlayerRepositoryImpl(player = getPlayer())
    private fun getPlayer(): Player = PlayerImpl(MediaPlayer())

}