package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder
    {
        return TrackViewHolder(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_track, parent, false))
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) = holder.bind(tracks[position])
    override fun getItemCount(): Int =tracks.size
}