package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.ui.search.model.SearchState
import com.practicum.playlistmaker.util.Creator

class SearchPresenter (context: Context) {
    private var view: SearchView? = null
    fun attachView(view: SearchView){ this.view = view }
    fun detachView() { this.view = null }

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
            view?.render(SearchState.Loading)

            trackInteractor.searchTrack(newSearchText, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTrackList: List<Track>?, errorMessage: String?) {
                    handler.post {
                        view?.showProgressBar(false)
                        if (foundTrackList != null) view?.render(SearchState.Content(foundTrackList))
                        else if (errorMessage != null) view?.render(SearchState.Error(errorMessage))
                    }
                }
            })
        } else { view?.render(SearchState.Content(listOf())) }
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}