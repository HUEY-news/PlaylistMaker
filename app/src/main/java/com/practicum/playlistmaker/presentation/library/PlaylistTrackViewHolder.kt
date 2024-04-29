package com.practicum.playlistmaker.presentation.library

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemTrackBinding
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.util.convertPixel
import com.practicum.playlistmaker.util.convertTime

class PlaylistTrackViewHolder(
    private val binding: ItemTrackBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        track: Track,
        onItemClick: (track: Track) -> Unit,
        onLongItemClick: (track: Track) -> Unit)
    {
        binding.root.setOnClickListener { onItemClick(track) }
        binding.root.setOnLongClickListener {
            onLongItemClick(track)
            true
        }

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = convertTime(track.trackTimeMillis)

        Glide
            .with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(convertPixel(4f, itemView.context)))
            .into(binding.artwork)
    }
}