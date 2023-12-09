package com.practicum.playlistmaker.searchHistory

import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.Debouncer
import com.practicum.playlistmaker.activity.PlayerActivity
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.trackList.TrackListViewHolder

class SearchHistoryAdapter(private var trackList: ArrayList<Track>): RecyclerView.Adapter<TrackListViewHolder>(){

    val debouncer = Debouncer()

    fun setTracks(tracks: ArrayList<Track>) {
        trackList = tracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder{
        return TrackListViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            Log.d("myLOG", "SearchHistoryAdapter item clicked!")
            if (debouncer.clickDebounce()) {
                // ЗАПУСКАЕТ PLAYER ACTIVITY И ПЕРЕДАЁТ ТРЕК
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra(PlayerActivity.TRACK_ID, trackList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}