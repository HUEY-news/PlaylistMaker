package com.practicum.playlistmaker.presentation.library

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistBinding
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.util.convertPixel

class PlaylistViewHolder(
    private val binding: ItemPlaylistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist) {
            binding.playlistName.text = playlist.playlistName
            binding.numberOfTracks.text = "${playlist.numberOfTracks} треков"

            Glide
                .with(itemView.context)
                .load(playlist.playlistCoverUri)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .transform(RoundedCorners(convertPixel(8f, itemView.context)))
                .into(binding.playlistImage)
        }
}