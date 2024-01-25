package com.practicum.playlistmaker.presentation.search.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.search.api.SearchInteractor
import com.practicum.playlistmaker.domain.track.api.TrackInteractor
import com.practicum.playlistmaker.domain.track.model.Track

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchInteractor: SearchInteractor
): ViewModel() {

    init { Log.v("TEST", "SearchViewModel СОЗДАНА") }

    private var searchStateLiveData = MutableLiveData<SearchState>()
    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private var searchHistoryLiveData = MutableLiveData<List<Track>>(getHistory())
    fun getSearchHistoryLiveData(): LiveData<List<Track>> = searchHistoryLiveData

    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null
    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText) }

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) return
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchRequest(newSearchText: String) {
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

    override fun onCleared() {
        Log.v("TEST", "SearchViewModel ОЧИЩЕНА")
        handler.removeCallbacks(searchRunnable)
    }

    private fun getHistory() = searchInteractor.getHistory()

    fun addTrackToHistory(track: Track) {
        searchInteractor.addTrackToHistory(track)
        searchHistoryLiveData.postValue(getHistory())
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}