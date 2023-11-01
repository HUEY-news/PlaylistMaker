package com.practicum.playlistmaker.searchHistory

import android.content.SharedPreferences
import com.practicum.playlistmaker.model.Track

// TODO: Всю логику, связанную с сохранением, чтением и очисткой истории, лучше описать в этом классе.

// TODO: Список треков в истории поиска выглядит точно так же, как и список результатов поиска.
//       Это значит, что можно не описывать для истории поиска новый ViewHolder,
//       а лишь создать отдельный экземпляр адаптера
//       и передавать ему сохранённый список треков для отображения истории.

class SearchHistory(sharedPreferences: SharedPreferences)
{
    private val searchHistoryTrackList = mutableListOf<Track>()

    fun getSearchHistoryTrackList(): ArrayList<Track>{
        return searchHistoryTrackList
    }
}