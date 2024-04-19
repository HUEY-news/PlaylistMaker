package com.practicum.playlistmaker.presentation.library

import com.practicum.playlistmaker.domain.search.Track

sealed interface FavoritePageState {
    object Empty: FavoritePageState
    data class Content(val data: List<Track>): FavoritePageState
}