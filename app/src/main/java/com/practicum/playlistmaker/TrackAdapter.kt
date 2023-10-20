package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.Track

class TrackAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) = holder.bind(tracks[position])
    override fun getItemCount(): Int =tracks.size
}