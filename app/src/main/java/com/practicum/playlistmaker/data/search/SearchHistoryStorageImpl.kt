package com.practicum.playlistmaker.data.search

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.search.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchHistoryStorageImpl @Inject constructor(
    private val preferences: SharedPreferences,
    private val gson: Gson,
    private val database: AppDatabase
): SearchHistoryStorage {

    private fun createJsonFromTrackList(trackList: ArrayList<Track>): String {
        return gson.toJson(trackList)
    }

    private fun saveHistory() {
        with(preferences.edit()) {
            putString(SEARCH_HISTORY_KEY, createJsonFromTrackList(history))
            apply()
        }
    }

    private fun createTrackListFromJson(json: String): ArrayList<Track> {
        val listType = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, listType)
    }

    private fun loadHistory() {
        val json = preferences.getString(SEARCH_HISTORY_KEY, null)
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
        with(preferences.edit()){
            remove(SEARCH_HISTORY_KEY)
            apply()
        }
    }

    override fun getHistory(): Flow<List<Track>> = flow {
        history.clear()
        loadHistory()
        val idList = database.favoriteTrackDao().getFavoriteIdList()
        for (track in history) track.isFavorite = idList.contains(track.trackId)
        emit(history)
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY"
        const val HISTORY_LIMIT = 10
        private val history = arrayListOf<Track>()
    }
}
