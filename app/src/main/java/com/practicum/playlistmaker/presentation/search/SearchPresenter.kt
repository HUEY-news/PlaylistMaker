package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.ui.search.model.SearchState
import com.practicum.playlistmaker.util.Creator
import moxy.MvpPresenter

class SearchPresenter (context: Context): MvpPresenter<SearchView>() {
    private fun renderState(state: SearchState) {
        viewState.render(state)
    }

    private val trackInteractor = Creator.provideTrackInteractor(context)
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null
//    private var latestSearchText: String? = null
    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText) }

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) return
//        this.latestSearchText = changedText
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            trackInteractor.searchTrack(newSearchText, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTrackList: List<Track>?, errorMessage: String?) {
                    handler.post {
                        if (foundTrackList != null) renderState(SearchState.Content(foundTrackList))
                        else if (errorMessage != null) renderState(SearchState.Error(errorMessage))
                    }
                }
            })
        } else { renderState(SearchState.Content(listOf())) }
    }

    override fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}