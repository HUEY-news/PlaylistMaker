package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.track.Track

interface SearchView {
    fun updateTrackList(newTrackList: List<Track>)
    fun clearTrackList()

    fun showPlaceholder(errorMessage: String)
    fun hidePlaceholder()

    fun showProgressBar(isVisible: Boolean)
    fun showSearchRecycler(isVisible: Boolean)

}