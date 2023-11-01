package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.model.Track

class TrackViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
        .inflate(R.layout.item_track, parent, false))
 {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val artWork: ImageView = itemView.findViewById(R.id.artwork)

    fun bind(track: Track)
    {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = trackTimeFormat(track.trackTimeMillis)

        Glide
            .with(itemView.context)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(pixelConverter(4f, itemView.context)))
            .into(artWork)
    }
}