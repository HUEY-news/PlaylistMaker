package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.SearchResponse
import com.practicum.playlistmaker.model.Track

 class TrackAdapter(private var trackList: List<Track>) : RecyclerView.Adapter<TrackViewHolder>()
 {
     fun setTracks(tracks: List<Track>) {
         trackList = tracks
         notifyDataSetChanged()
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) = holder.bind(trackList[position])
    override fun getItemCount(): Int = trackList.size
 }
