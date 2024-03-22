package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.search.Track

sealed interface SearchState {
    object Loading: SearchState
    data class Content (val data: List<Track>): SearchState
    data class Error (val message: String): SearchState
}