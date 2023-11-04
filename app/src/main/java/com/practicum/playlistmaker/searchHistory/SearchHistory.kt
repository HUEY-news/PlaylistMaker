package com.practicum.playlistmaker.searchHistory

import android.content.SharedPreferences
import android.util.Log
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
        Log.d("TEST", "ПРОВЕРКА РАЗМЕРА ИСТОРИИ")
        if (history.size > HISTORY_LIMIT) {
            history.forEachIndexed { index: Int, item: Track ->
                if (index > HISTORY_LIMIT - 1) history.remove(item)
            }
        }
    }

    private fun addUnique(track: Track){
        Log.d("TEST", "ДОБАВЛЕНИЕ ТРЕКА В ИСТОРИЮ")
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

    private fun saveHistory() {
        Log.d("TEST", "СОХРАНЕНИЕ ИСТОРИИ")
        with(sharedPreferences.edit()) {
            putString(TRACK_LIST_KEY, createJsonFromFTrackList(history))
            apply()
        }
    }

    private fun loadHistory() {
        Log.d("TEST", "ЗАГРУЗКА ИСТОРИИ")
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null)
        if (json != null) history.addAll(createTrackListFromJson(json))
    }

    fun showHistory(){
        loadHistory()
        adapter.setTracks(history)
    }

    fun clearHistory() {
        Log.d("TEST", "ОЧИСТКА ИСТОРИИ")
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
