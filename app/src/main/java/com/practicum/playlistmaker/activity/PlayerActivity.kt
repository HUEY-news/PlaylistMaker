package com.practicum.playlistmaker.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.pixelConverter
import com.practicum.playlistmaker.trackTimeFormat

const val TRACK_ID = "TRACK_ID"

class PlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<ImageButton>(R.id.button_back).setOnClickListener { finish() }

        val artWork = findViewById<ImageView>(R.id.image_view_artwork_512)
        val trackName = findViewById<TextView>(R.id.text_view_track_name)
        val artistName = findViewById<TextView>(R.id.text_view_artist_name)
        val trackTime = findViewById<TextView>(R.id.text_view_track_time)
        val trackDuration = findViewById<TextView>(R.id.text_view_track_info_duration_content)
        val trackAlbum = findViewById<TextView>(R.id.text_view_track_info_album_content)
        val trackYear = findViewById<TextView>(R.id.text_view_track_info_year_content)
        val trackGenre = findViewById<TextView>(R.id.text_view_track_info_genre_content)
        val trackCountry = findViewById<TextView>(R.id.text_view_track_info_country_content)

        val track: Track?
        val json = intent.getStringExtra(TRACK_ID)
        fun createTrackFromJson(json: String): Track = Gson().fromJson(json, Track::class.java)
        track = if (json != null) createTrackFromJson(json) else null

        Glide
            .with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .centerCrop()
            .transform(RoundedCorners(pixelConverter(4f, this)))
            .into(artWork)

        trackTime.text = trackTimeFormat(track?.trackTimeMillis!!)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = trackTimeFormat(track.trackTimeMillis)
        trackAlbum.text = track.collectionName
        trackYear.text = track.releaseDate
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

    }
}


