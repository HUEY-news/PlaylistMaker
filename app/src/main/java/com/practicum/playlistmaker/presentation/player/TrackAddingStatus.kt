package com.practicum.playlistmaker.presentation.player

import com.practicum.playlistmaker.domain.library.Playlist

sealed interface TrackAddingStatus {

    object Ready: TrackAddingStatus
    data class Done(val playlist: Playlist): TrackAddingStatus
    data class NotDone(val playlist: Playlist): TrackAddingStatus
}