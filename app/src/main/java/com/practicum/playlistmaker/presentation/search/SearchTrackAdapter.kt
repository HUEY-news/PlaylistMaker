package com.practicum.playlistmaker.presentation.search

import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.utils.Debouncer
import com.practicum.playlistmaker.presentation.player.PlayerActivity
import com.practicum.playlistmaker.domain.models.Track

class SearchTrackAdapter(private var trackList: ArrayList<Track>) : RecyclerView.Adapter<SearchTrackViewHolder>() {

    val debouncer = Debouncer()

    fun setTracks(tracks: ArrayList<Track>) {
        trackList = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        return SearchTrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            Log.d("myLOG", "TrackListAdapter item clicked!")
            if (debouncer.clickDebounce()) {

                // ДОБАВЛЯЕТ ТРЕК В ИСТОРИЮ ПОИСКА
                val searchHistory = SearchHistory(App.sharedPreferences)
                searchHistory.addTrackToHistory(trackList[position])

                // ЗАПУСКАЕТ PLAYER ACTIVITY И ПЕРЕДАЁТ ТРЕК
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra(PlayerActivity.TRACK_ID, trackList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}
