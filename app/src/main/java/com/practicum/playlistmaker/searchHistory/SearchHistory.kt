package com.practicum.playlistmaker.searchHistory

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.trackList.TrackListAdapter

const val TRACK_LIST_KEY = "TRACK_LIST"
const val HISTORY_LIMIT = 10

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val historyList = arrayListOf<Track>()
    val adapter = TrackListAdapter(historyList)

    private fun sizeCheck(){
        if (historyList.size > HISTORY_LIMIT) {
            historyList.forEachIndexed { index: Int, item: Track ->
                if (index > HISTORY_LIMIT - 1) historyList.remove(
                    item
                )
            }
        }
    }

    private fun addUnique(track: Track){
        historyList.forEach {
            if (track.trackId == it.trackId){
                historyList.remove(it)
            }
            historyList.add(0,track)
        }
    }

    fun addTrackToHistory(track: Track) {
        loadHistoryList()
        addUnique(track)
        sizeCheck()
        adapter.setTracks(historyList)
        saveHistoryList()
    }

    fun saveHistoryList() {
        with(sharedPreferences.edit()) {
            putString(TRACK_LIST_KEY, createJsonFromFTrackList(historyList))
            apply()
        }
    }

    fun loadHistoryList() {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null)
        if (json != null) historyList.addAll(createTrackListFromJson(json))
    }

    fun clearHistory() {
        historyList.clear()
        adapter.setTracks(historyList)
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
