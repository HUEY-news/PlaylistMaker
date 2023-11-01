package com.practicum.playlistmaker.searchHistory

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.trackList.TrackListAdapter

const val TRACK_LIST_KEY = "TRACK_LIST"

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    private val searchHistoryTrackList = arrayListOf<Track>()
    val adapter = TrackListAdapter(searchHistoryTrackList)

    private fun addTrackToHistory(track: Track) {
        searchHistoryTrackList.add(0, track)
        adapter.setTracks(searchHistoryTrackList)
        saveHistory()
    }

    private fun saveHistory() {
        with(sharedPreferences.edit()) {
            putString(TRACK_LIST_KEY, createJsonFromFTrackList(searchHistoryTrackList))
            apply()
        }
    }

    private fun loadHistory() {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null)
        if (json != null) searchHistoryTrackList.addAll(createTrackListFromJson(json))
        adapter.setTracks(searchHistoryTrackList)
    }

    private fun clearHistory() {
        searchHistoryTrackList.clear()
        adapter.setTracks(searchHistoryTrackList)
        sharedPreferences.edit().clear()

    }

    // TODO: достать список треков из JSON:
    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, listType)
    }

    // TODO: поместить список треков в JSON:
    private fun createJsonFromFTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }
}
