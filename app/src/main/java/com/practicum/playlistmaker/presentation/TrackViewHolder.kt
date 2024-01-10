package com.practicum.playlistmaker.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.utils.convertPixel
import com.practicum.playlistmaker.utils.convertTime

class TrackViewHolder(
    itemView: View,
    onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            onItemClick(adapterPosition)
        }
    }

    fun bind(track: Track)
    {
        itemView.findViewById<TextView>(R.id.trackName).text = track.trackName
        itemView.findViewById<TextView>(R.id.artistName).text = track.artistName
        itemView.findViewById<TextView>(R.id.trackTime).text = convertTime(track.trackTimeMillis)

        Glide
            .with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(convertPixel(4f, itemView.context)))
            .into(itemView.findViewById<ImageView>(R.id.artwork))
    }
}