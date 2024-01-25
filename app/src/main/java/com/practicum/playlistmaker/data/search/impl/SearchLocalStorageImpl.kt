package com.practicum.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.search.api.SearchLocalStorage
import com.practicum.playlistmaker.domain.track.model.Track

class SearchLocalStorageImpl(
    private val sharedPreferences: SharedPreferences
): SearchLocalStorage {

    private fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }

    private fun saveHistory() {
        with(sharedPreferences.edit()) {
            putString(SEARCH_HISTORY_KEY, createJsonFromTrackList(history))
            apply()
        }
    }

    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun loadHistory() {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        if (json != null) history.addAll(createTrackListFromJson(json))
    }

    override fun addTrackToHistory(track: Track) {
        history.clear()
        loadHistory()
        addUnique(track)
        sizeCheck()
        saveHistory()
    }

    private fun addUnique(track: Track){
        val iterator = history.iterator()
        while (iterator.hasNext()){
            val item = iterator.next()
            if (track.trackId == item.trackId) iterator.remove()
        }
        history.add(0,track)
    }
    private fun sizeCheck(){
        if (history.size > HISTORY_LIMIT) {
            history.forEachIndexed { index: Int, item: Track ->
                if (index > HISTORY_LIMIT - 1) history.remove(item)
            }
        }
    }

    override fun clearHistory() {
        with(sharedPreferences.edit()){
            remove(SEARCH_HISTORY_KEY)
            apply()
        }
    }

    override fun getHistory(): ArrayList<Track> {
        history.clear()
        loadHistory()
        return history
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
        const val HISTORY_LIMIT = 10
        private val history = arrayListOf<Track>()
    }
}