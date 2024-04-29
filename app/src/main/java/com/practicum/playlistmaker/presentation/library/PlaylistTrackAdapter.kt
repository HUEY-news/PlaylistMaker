package com.practicum.playlistmaker.presentation.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.domain.search.Track

class PlaylistTrackAdapter(
    private val onItemClick: (track: Track) -> Unit,
    private val onLongItemClick: (track: Track) -> Unit
): RecyclerView.Adapter<PlaylistTrackViewHolder>() {

    private var itemList: List<Track> = emptyList()

    fun setItems(items: List<Track>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrackBinding.inflate(layoutInflater, parent, false)
        return PlaylistTrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        itemList.getOrNull(position)?.let { track ->
            holder.bind(track, onItemClick, onLongItemClick)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}