package com.practicum.playlistmaker.data.search

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.track.Track

const val TRACK_LIST_KEY = "TRACK_LIST"
const val HISTORY_LIMIT = 10

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return Gson().toJson(trackList)
    }
    private fun saveHistory() {
        Log.d("TEST", "fun saveHistory() activated")
        with(sharedPreferences.edit()) {
            putString(TRACK_LIST_KEY, createJsonFromTrackList(history))
            apply()
        }
    }

    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, listType)
    }
    private fun loadHistory() {
        Log.d("TEST", "fun loadHistory() activated")
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null)
        if (json != null) history.addAll(createTrackListFromJson(json))
    }

    fun addTrackToHistory(track: Track) {
        history.clear()
        loadHistory()
        addUnique(track)
        sizeCheck()
        saveHistory()
    }
    private fun addUnique(track: Track){
        Log.d("TEST", "fun addUnique(track: Track) activated")
        val iterator = history.iterator()
        while (iterator.hasNext()){
            val item = iterator.next()
            if (track.trackId == item.trackId) iterator.remove()
        }
            history.add(0,track)
    }
    private fun sizeCheck(){
        Log.d("TEST", "fun sizeCheck() activated")
        if (history.size > HISTORY_LIMIT) {
            history.forEachIndexed { index: Int, item: Track ->
                if (index > HISTORY_LIMIT - 1) history.remove(item)
            }
        }
    }

    fun clearHistory() {
        Log.d("TEST", "fun clearHistory() activated")
        with(sharedPreferences.edit()){
            remove(TRACK_LIST_KEY)
            apply()
        }
    }

    fun getHistory(): ArrayList<Track>{
        history.clear()
        loadHistory()
        return history
    }

    companion object{
        private val history = arrayListOf<Track>()
    }
}
