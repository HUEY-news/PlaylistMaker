package com.practicum.playlistmaker.trackList

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.Track

 class TrackListAdapter(private var trackList: List<Track>) : RecyclerView.Adapter<TrackListViewHolder>()
 {
     fun setTracks(tracks: List<Track>) {
         trackList = tracks
         notifyDataSetChanged()
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder = TrackListViewHolder(parent)
    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) = holder.bind(trackList[position])
    override fun getItemCount(): Int = trackList.size
 }
