package com.practicum.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.app.PREFERENCES_FOLDER_NAME
import com.practicum.playlistmaker.convertor.DbConvertor
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providePlayer(mediaPlayer: MediaPlayer): Player {
        return PlayerImpl(mediaPlayer = mediaPlayer)
    }

    @Provides
    fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    @Provides
    fun provideExternalNavigator(@ApplicationContext context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context = context)
    }

    @Provides
    fun provideSettingsLocalStorage(preferences: SharedPreferences): SettingsLocalStorage {
        return SettingsLocalStorageImpl(preferences = preferences)
    }

    @Provides
    fun provideSearchHistoryStorage(preferences: SharedPreferences, gson: Gson, database: AppDatabase): SearchHistoryStorage {
        return SearchHistoryStorageImpl(preferences = preferences, gson = gson, database = database)
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FOLDER_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideProductDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database.db").build()
    }

    @Provides
    fun provideDatabaseConverter(): DbConvertor {
        return DbConvertor()
    }

    @Provides
    fun provideNetworkClient(@ApplicationContext context: Context, service: iTunesApiService): NetworkClient {
        return RetrofitNetworkClient(context = context, service = service)
    }

    @Provides
    fun provideApiService(): iTunesApiService {
        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApiService::class.java)
    }

}
