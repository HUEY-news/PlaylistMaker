package com.practicum.playlistmaker.presentation.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.domain.search.Track

class TrackAdapter(
    private val onItemClick: (track: Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var itemList: List<Track> = emptyList()

    fun setItems(items: List<Track>) {
        itemList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(ItemTrackBinding.inflate(layoutInspector, parent, false))
        { position: Int ->
            if (position != RecyclerView.NO_POSITION) {
                itemList.getOrNull(position)?.let { track ->
                    onItemClick(track)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        itemList.getOrNull(position)?.let { track ->
            holder.bind(track)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}