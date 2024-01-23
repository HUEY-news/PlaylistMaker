package com.practicum.playlistmaker.presentation.search.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.domain.track.Track

class SearchAdapter(
    private val onItemClick: (track: Track) -> Unit
) : RecyclerView.Adapter<SearchViewHolder>() {
    private var trackList: List<Track> = emptyList()

    fun setItems(items: List<Track>) {
        trackList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchViewHolder(ItemTrackBinding.inflate(layoutInspector, parent, false))
        { position: Int ->
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

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        trackList.getOrNull(position)?.let { track ->
            holder.bind(track)
        }
    }
}
