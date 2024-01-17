package com.practicum.playlistmaker.ui.search.model

import com.practicum.playlistmaker.domain.track.Track

data class SearchState(
    val trackList: List<Track>,
    val isLoading: Boolean,
    val errorMessage: String?
)
