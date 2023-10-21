package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.model.SearchResponse

class TrackAdapter(private val response: SearchResponse) : RecyclerView.Adapter<TrackViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) = holder.bind(response.results[position])
    override fun getItemCount(): Int = response.resultCount
}
