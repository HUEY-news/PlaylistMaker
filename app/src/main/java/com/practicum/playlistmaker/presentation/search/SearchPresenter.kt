package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.ui.search.TrackAdapter
import com.practicum.playlistmaker.util.Creator

class SearchPresenter (
    private val view: SearchView,
    private val context: Context,
    private val searchAdapter: TrackAdapter
) {
    private val trackInteractor = Creator.provideTrackInteractor(context)
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null
    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText) }

    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            view.hidePlaceholder()
            view.clearTrackList()
            view.showProgressBar(true)

            trackInteractor.searchTrack(newSearchText, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTrackList: List<Track>?, errorMessage: String?) {
                    handler.post {
                        view.showProgressBar(false)
                        if (foundTrackList != null) {
                            searchAdapter.setItems(foundTrackList)
                            view.showSearchRecycler(true)
                        } else if (errorMessage != null) view.showPlaceholder(errorMessage)
                    }
                }
            })
        } else { view.clearTrackList() }
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}