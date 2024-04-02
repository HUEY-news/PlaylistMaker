package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.app.PREFERENCES_FOLDER_NAME
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.iTunesApiService
import com.practicum.playlistmaker.data.player.Player
import com.practicum.playlistmaker.data.player.PlayerImpl
import com.practicum.playlistmaker.data.search.SearchHistoryStorage
import com.practicum.playlistmaker.data.search.SearchHistoryStorageImpl
import com.practicum.playlistmaker.data.settings.ExternalNavigator
import com.practicum.playlistmaker.data.settings.ExternalNavigatorImpl
import com.practicum.playlistmaker.data.settings.SettingsLocalStorage
import com.practicum.playlistmaker.data.settings.SettingsLocalStorageImpl
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
    single<SearchHistoryStorage> { SearchHistoryStorageImpl(prefs = get(), gson = get(), appDatabase = get()) }
    single { androidContext().getSharedPreferences(PREFERENCES_FOLDER_NAME, Context.MODE_PRIVATE) }
    single { Gson() }

    single<Player> { PlayerImpl(mediaPlayer = MediaPlayer()) }
    single { MediaPlayer() }

    single<ExternalNavigator> { ExternalNavigatorImpl(context = androidContext()) }
    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build() }
}