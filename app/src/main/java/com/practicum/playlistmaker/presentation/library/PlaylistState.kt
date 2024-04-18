package com.practicum.playlistmaker.presentation.library

import com.practicum.playlistmaker.domain.library.Playlist

sealed interface PlaylistState {
    object Empty: PlaylistState
    data class Content(val data: List<Playlist>): PlaylistState
}