package com.practicum.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Track

class SearchTrackAdapter(
    private val onItemClick: (track: Track) -> Unit
) : RecyclerView.Adapter<SearchTrackViewHolder>() {
    private var trackList: List<Track> = emptyList()

    fun setItems(items: List<Track>) {
        trackList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return SearchTrackViewHolder(itemView) { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                trackList.getOrNull(position)?.let { track ->
                    onItemClick(track)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        trackList.getOrNull(position)?.let { track ->
            holder.bind(track)
        }
    }
}
