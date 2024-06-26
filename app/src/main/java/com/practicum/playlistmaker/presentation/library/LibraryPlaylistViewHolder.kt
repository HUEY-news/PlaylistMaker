package com.practicum.playlistmaker.presentation.library

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlaylistLibraryBinding
import com.practicum.playlistmaker.domain.library.Playlist

class LibraryPlaylistViewHolder(
    private val binding: ItemPlaylistLibraryBinding,
    onItemClick: (position: Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(playlist: Playlist) {
            binding.playlistName.text = playlist.playlistName
            binding.numberOfTracks.text = declensionByCase(playlist.numberOfTracks)

            Glide
                .with(itemView.context)
                .load(playlist.playlistCoverUri)
                .placeholder(R.drawable.ic_placeholder_45)
                .centerCrop()
                .into(binding.playlistImage)
        }

    private fun declensionByCase(number: Int): String {
        val preLastDigit = number % 100 / 10
        if (preLastDigit == 1) return "$number треков"

        return when (number % 10) {
            1 -> "$number трек"
            2 -> "$number трека"
            3 -> "$number трека"
            4 -> "$number трека"
            else -> "$number треков"
        }
    }
}