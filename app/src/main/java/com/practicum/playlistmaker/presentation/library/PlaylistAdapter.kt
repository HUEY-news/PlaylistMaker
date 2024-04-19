package com.practicum.playlistmaker.presentation.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.domain.library.Playlist

class PlaylistAdapter: RecyclerView.Adapter<PlaylistViewHolder>() {

    private var playlistList: List<Playlist> = emptyList()

    fun setItems(items: List<Playlist>) {
        playlistList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemPlaylistBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        playlistList.getOrNull(position)?.let { playlist ->
            holder.bind(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }
}