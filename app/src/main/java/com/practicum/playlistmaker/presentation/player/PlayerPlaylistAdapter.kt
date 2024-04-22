package com.practicum.playlistmaker.presentation.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemPlaylistPlayerBinding
import com.practicum.playlistmaker.domain.library.Playlist

class PlayerPlaylistAdapter(
    private val onItemClick: (playlist: Playlist) -> Unit
): RecyclerView.Adapter<PlayerPlaylistViewHolder>() {

    private var itemList: List<Playlist> = emptyList()

    fun setItems(items: List<Playlist>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerPlaylistViewHolder(ItemPlaylistPlayerBinding.inflate(layoutInspector, parent, false))
        { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                itemList.getOrNull(position)?.let {  playlist ->
                    onItemClick(playlist)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PlayerPlaylistViewHolder, position: Int) {
        itemList.getOrNull(position)?.let { playlist ->
            holder.bind(playlist)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}