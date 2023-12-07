package com.practicum.playlistmaker.searchHistory

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Debounce
import com.practicum.playlistmaker.activity.PlayerActivity
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.trackList.TrackListViewHolder

class SearchHistoryAdapter(private var trackList: ArrayList<Track>): RecyclerView.Adapter<TrackListViewHolder>(){

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
            if (Debounce().clickDebounce()) {
                // ЗАПУСКАЕТ PLAYER ACTIVITY И ПЕРЕДАЁТ ТРЕК В ФОРМАТЕ JSON
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java)
                intent.putExtra(PlayerActivity.TRACK_ID, trackList[position])
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}