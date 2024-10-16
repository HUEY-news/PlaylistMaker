package com.practicum.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.convertor.DbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.library.FavoriteRepositoryImpl
import com.practicum.playlistmaker.data.library.PlaylistRepositoryImpl
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.player.Player
import com.practicum.playlistmaker.data.player.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.SearchHistoryStorage
import com.practicum.playlistmaker.data.search.TrackRepositoryImpl
import com.practicum.playlistmaker.data.settings.ExternalNavigator
import com.practicum.playlistmaker.data.settings.SettingsLocalStorage
import com.practicum.playlistmaker.data.settings.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.settings.SharingRepositoryImpl
import com.practicum.playlistmaker.domain.library.FavoriteRepository
import com.practicum.playlistmaker.domain.library.PlaylistRepository
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.SharingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideTrackRepository(@ApplicationContext context: Context, client: NetworkClient, database: AppDatabase): TrackRepository {
        return TrackRepositoryImpl(context = context, client = client, database = database)
    }

    @Provides
    fun provideSearchHistoryRepository(storage: SearchHistoryStorage): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(storage = storage)
    }

    @Provides
    fun providePlayerRepository(player: Player): PlayerRepository {
        return PlayerRepositoryImpl(player = player)
    }

    @Provides
    fun provideSharingRepository(navigator: ExternalNavigator): SharingRepository {
        return SharingRepositoryImpl(navigator = navigator)
    }

    @Provides
    fun provideSettingsRepository(storage: SettingsLocalStorage): SettingsRepository {
        return SettingsRepositoryImpl(storage = storage)
    }

    @Provides
    fun provideFavoriteRepository(database: AppDatabase, convertor: DbConvertor): FavoriteRepository {
        return FavoriteRepositoryImpl(database = database, convertor = convertor)
    }

    @Provides
    fun providePlaylistRepository(database: AppDatabase, convertor: DbConvertor, navigator: ExternalNavigator, gson: Gson): PlaylistRepository {
        return PlaylistRepositoryImpl(database = database, convertor = convertor, navigator = navigator, gson = gson)
    }

}
