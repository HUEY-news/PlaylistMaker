package com.practicum.playlistmaker

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(item: View) : RecyclerView.ViewHolder(item)
{
    private val trackName: TextView = item.findViewById(R.id.TV_track_name)
    private val artistName: TextView = item.findViewById(R.id.TV_artist_name)
    private val trackTime: TextView = item.findViewById(R.id.TV_track_time)
    private val artWork: ImageView = item.findViewById(R.id.IV_artwork)

    fun bind(data: Track)
    {
        trackName.text = data.trackName
        artistName.text = data.artistName
        trackTime.text = data.trackTime

        Glide
            .with(itemView.context)
            .load(data.artWorkUrl100)
            .placeholder(R.drawable.ic_test_background)
            .centerCrop()
            .transform(RoundedCorners(pixelConverter(4f, itemView.context)))
            .into(artWork)
    }
}