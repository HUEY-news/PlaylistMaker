package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.ui.search.model.SearchState

interface SearchView {

    fun render(state: SearchState)

    // методы, меняющие внешний вид экрана
    fun updateTrackList(trackList: List<Track>)
    fun showProgressBar(isVisible: Boolean)

    // методы одноразовых событий
    fun hidePlaceholder()

}