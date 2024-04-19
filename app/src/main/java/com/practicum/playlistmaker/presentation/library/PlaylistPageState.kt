package com.practicum.playlistmaker.presentation.library

import com.practicum.playlistmaker.domain.library.Playlist

sealed interface PlaylistPageState {
    object Empty: PlaylistPageState
    data class Content(val data: List<Playlist>): PlaylistPageState
}