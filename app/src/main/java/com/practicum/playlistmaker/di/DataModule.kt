package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.app.PREFERENCES_FOLDER_NAME
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.iTunesApiService
import com.practicum.playlistmaker.data.player.api.Player
import com.practicum.playlistmaker.data.player.impl.PlayerImpl
import com.practicum.playlistmaker.data.search.api.SearchHistoryStorage
import com.practicum.playlistmaker.data.search.impl.SearchHistoryStorageImpl
import com.practicum.playlistmaker.data.settings.api.ExternalNavigator
import com.practicum.playlistmaker.data.settings.api.SettingsLocalStorage
import com.practicum.playlistmaker.data.settings.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.settings.impl.SettingsLocalStorageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<NetworkClient> { RetrofitNetworkClient(context = androidContext(), service = get()) }
    single<iTunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApiService::class.java)
    }

    single<SettingsLocalStorage> { SettingsLocalStorageImpl(prefs = get()) }
    single<SearchHistoryStorage> { SearchHistoryStorageImpl(prefs = get(), gson = get()) }
    single { androidContext().getSharedPreferences(PREFERENCES_FOLDER_NAME, Context.MODE_PRIVATE) }
    single { Gson() }

    single<Player> { PlayerImpl(player = MediaPlayer()) }
    single { MediaPlayer() }

    single<ExternalNavigator> { ExternalNavigatorImpl(context = androidContext()) }
}