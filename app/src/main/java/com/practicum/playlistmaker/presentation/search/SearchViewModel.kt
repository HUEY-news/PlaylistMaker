package com.practicum.playlistmaker.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.track.api.TrackInteractor
import com.practicum.playlistmaker.domain.track.model.Track
import com.practicum.playlistmaker.util.debounce

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchInteractor: SearchInteractor
): ViewModel() {

    init { Log.v("TEST", "SearchViewModel СОЗДАНА") }

    private var searchStateLiveData = MutableLiveData<SearchState>()
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private var searchHistoryLiveData = MutableLiveData<List<Track>>(getHistory())
    fun getSearchHistoryLiveData(): LiveData<List<Track>> = searchHistoryLiveData

    private var lastQuery: String? = null

    val trackSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_MILLIS,
        viewModelScope,
        true
    ) { text -> searchTrack(text) }

    fun onSearchDebounce(text: String) {
        if (lastQuery != text) {
            lastQuery = text
            val currentQuery = lastQuery ?: ""
            trackSearchDebounce(currentQuery)
        }
    }

    fun searchTrack(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            searchStateLiveData.postValue(SearchState.Loading)

            trackInteractor.searchTrack(newSearchText, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTrackList: List<Track>?, errorMessage: String?) {
                    handler.post {
                        if (foundTrackList != null) {
                            searchStateLiveData.value = SearchState.Content(foundTrackList)
                        }
                        else if (errorMessage != null) {
                            searchStateLiveData.value = SearchState.Error(errorMessage)
                        }
                    }
                }
            })
        } else { searchStateLiveData.postValue(SearchState.Content(listOf())) }
    }

    fun addTrackToHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
        searchHistoryLiveData.postValue(getHistory())
    }
    private fun getHistory() = searchInteractor.getHistory()
    fun clearHistory() { searchInteractor.clearHistory() }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}