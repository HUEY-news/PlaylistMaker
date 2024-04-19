package com.practicum.playlistmaker.presentation.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemPlaylistLibraryBinding
import com.practicum.playlistmaker.domain.library.Playlist

class LibraryPlaylistAdapter: RecyclerView.Adapter<LibraryPlaylistViewHolder>() {

    private var itemList: List<Playlist> = emptyList()

    fun setItems(items: List<Playlist>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryPlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return LibraryPlaylistViewHolder(ItemPlaylistLibraryBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(holder: LibraryPlaylistViewHolder, position: Int) {
        itemList.getOrNull(position)?.let { playlist ->
            holder.bind(playlist)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}