package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.model.Track

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_track, parent, false)
) {
    private val trackName: TextView = parent.findViewById(R.id.TV_track_name)
    private val artistName: TextView = parent.findViewById(R.id.TV_artist_name)
    private val trackTime: TextView = parent.findViewById(R.id.TV_track_time)
    private val artWork: ImageView = parent.findViewById(R.id.IV_artwork)

    fun bind(data: Track) {
        trackName.text = data.trackName
        artistName.text = data.artistName
        trackTime.text = data.trackTime

        Glide
            .with(itemView.context)
            .load(data.artWorkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(pixelConverter(4f, itemView.context)))
            .into(artWork)
    }
}