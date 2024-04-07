package com.practicum.playlistmaker.presentation.library

import com.practicum.playlistmaker.domain.search.Track

sealed interface FavoriteState {
    object Empty: FavoriteState
    data class Content(val trackList: List<Track>): FavoriteState
}