package com.practicum.playlistmaker.presentation.search

interface SearchView {
    fun showPlaceholder(errorMessage: String)
    fun hidePlaceholder()

    fun clearTrackList()

    fun showProgressBar(isVisible: Boolean)
    fun showSearchRecycler(isVisible: Boolean)

}