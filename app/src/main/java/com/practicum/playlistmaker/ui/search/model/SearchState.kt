package com.practicum.playlistmaker.ui.search.model

import com.practicum.playlistmaker.domain.track.Track

// реализация концепции LCE через state:
sealed interface SearchState {
    object Loading: SearchState
    data class Content (val trackList: List<Track>): SearchState
    data class Error (val errorMessage: String): SearchState
}