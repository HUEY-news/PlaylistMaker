package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.library.FavoriteInteractor
import com.practicum.playlistmaker.domain.library.FavoriteInteractorImpl
import com.practicum.playlistmaker.domain.library.FavoriteRepository
import com.practicum.playlistmaker.domain.library.PlaylistInteractor
import com.practicum.playlistmaker.domain.library.PlaylistInteractorImpl
import com.practicum.playlistmaker.domain.library.PlaylistRepository
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.TrackInteractor
import com.practicum.playlistmaker.domain.search.TrackInteractorImpl
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.SharingInteractor
import com.practicum.playlistmaker.domain.settings.SharingInteractorImpl
import com.practicum.playlistmaker.domain.settings.SharingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class InteractorModule {

    @Provides
    fun provideTrackInteractor(repository: TrackRepository): TrackInteractor {
        return TrackInteractorImpl(repository = repository)
    }

    @Provides
    fun provideSearchHistoryInteractor(repository: SearchHistoryRepository): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(repository = repository)
    }

    @Provides
    fun providePlayerInteractor(repository: PlayerRepository): PlayerInteractor {
        return PlayerInteractorImpl(repository = repository)
    }

    @Provides
    fun provideSharingInteractor(repository: SharingRepository): SharingInteractor {
        return SharingInteractorImpl(repository = repository)
    }

    @Provides
    fun provideSettingsInteractor(repository: SettingsRepository): SettingsInteractor {
        return SettingsInteractorImpl(repository = repository)
    }

    @Provides
    fun provideFavoriteInteractor(repository: FavoriteRepository): FavoriteInteractor {
        return FavoriteInteractorImpl(repository = repository)
    }

    @Provides
    fun providePlaylistInteractor(repository: PlaylistRepository): PlaylistInteractor {
        return PlaylistInteractorImpl(repository = repository)
    }

}
