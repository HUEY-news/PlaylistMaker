package com.practicum.playlistmaker.presentation.search

import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.ui.search.model.SearchState

interface SearchView {

    // реализация методов схемы LCE через state
    fun render(state: SearchState)

    // методы, меняющие внешний вид экрана
    fun updateTrackList(trackList: List<Track>)
    fun showProgressBar(isVisible: Boolean)

}