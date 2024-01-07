package com.practicum.playlistmaker.presentation.ui.search

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.ui.player.PlayerActivity

class SearchTrackAdapter(private var trackList: List<Track>) : RecyclerView.Adapter<SearchTrackViewHolder>() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun setTracks(tracks: List<Track>) {
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
            if (clickDebounce()) {

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

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
