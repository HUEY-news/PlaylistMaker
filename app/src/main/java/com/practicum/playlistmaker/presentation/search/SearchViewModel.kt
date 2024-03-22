package com.practicum.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.domain.search.TrackInteractor
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
): ViewModel() {

    private var lastQuery: String? = null

    private var searchStateLiveData = MutableLiveData<SearchState>()
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData
    private var searchHistoryLiveData = MutableLiveData<List<Track>>(getHistory())
    fun getSearchHistoryLiveData(): LiveData<List<Track>> = searchHistoryLiveData

    private fun renderState(state: SearchState) {
        searchStateLiveData.postValue(state)
    }

    fun searchTrack(query: String) {
        if (query.isNotEmpty()) {
            renderState(SearchState.Loading)
        } else renderState(SearchState.Content(listOf()))
    }

    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrackToHistory(track)
        searchHistoryLiveData.postValue(getHistory())
    }

    private fun getHistory(): List<Track> {
        var trackList = emptyList<Track>()
        viewModelScope.launch {
            searchHistoryInteractor
                .getHistory()
                .collect { trackList = it }
        }
        return trackList
    }

    fun clearHistory() { searchHistoryInteractor.clearHistory() }

    fun searchDebounce(text: String) {
        if (lastQuery != text) {
            lastQuery = text
            val currentQuery = lastQuery ?: ""
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}