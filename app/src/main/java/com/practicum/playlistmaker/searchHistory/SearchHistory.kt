package com.practicum.playlistmaker.searchHistory

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.trackList.TrackListAdapter

const val TRACK_LIST_KEY = "TRACK_LIST"
const val HISTORY_LIMIT = 10

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val history = arrayListOf<Track>()
    val adapter = TrackListAdapter(history)

    private fun sizeCheck(){
        if (history.size > HISTORY_LIMIT) {
            history.forEachIndexed { index: Int, item: Track ->
                if (index > HISTORY_LIMIT - 1) history.remove(
                    item
                )
            }
        }
    }

    private fun addUnique(track: Track){
        history.forEach {
            if (track.trackId == it.trackId){
                history.remove(it)
            }
            history.add(0,track)
        }
    }

    fun addTrackToHistory(track: Track) {
        loadHistory()
        addUnique(track)
        sizeCheck()
        adapter.setTracks(history)
        saveHistory()
    }

    fun saveHistory() {
        with(sharedPreferences.edit()) {
            putString(TRACK_LIST_KEY, createJsonFromFTrackList(history))
            apply()
        }
    }

    fun loadHistory() {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null)
        if (json != null) history.addAll(createTrackListFromJson(json))
    }

    fun clearHistory() {
        history.clear()
        adapter.setTracks(history)
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
