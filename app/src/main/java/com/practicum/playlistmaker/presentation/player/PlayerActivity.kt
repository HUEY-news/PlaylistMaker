package com.practicum.playlistmaker.presentation.player

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.utils.pixelConverter
import com.practicum.playlistmaker.utils.trackTimeFormat

class PlayerActivity : AppCompatActivity() {

    private lateinit var buttonPlayPause: ImageButton
    private var url: String? = null
    private lateinit var trackTimer: TextView
    private var mediaPlayer = MediaPlayer()
    private lateinit var trackPlayer: TrackPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<ImageButton>(R.id.button_back).setOnClickListener { finish() }

        val artWork = findViewById<ImageView>(R.id.image_view_artwork_512)
        val trackName = findViewById<TextView>(R.id.text_view_track_name)
        val artistName = findViewById<TextView>(R.id.text_view_artist_name)
        trackTimer = findViewById(R.id.text_view_track_timer)
        val trackDuration = findViewById<TextView>(R.id.text_view_track_info_duration_content)
        val trackAlbum = findViewById<TextView>(R.id.text_view_track_info_album_content)
        val trackYear = findViewById<TextView>(R.id.text_view_track_info_year_content)
        val trackGenre = findViewById<TextView>(R.id.text_view_track_info_genre_content)
        val trackCountry = findViewById<TextView>(R.id.text_view_track_info_country_content)

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_ID, Track::class.java)
        } else { // IF VERSION.SDK_INT < TIRAMISU
            intent.getParcelableExtra(TRACK_ID)
        }

        url = track?.previewUrl.toString()

        Glide
            .with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(pixelConverter(4f, this)))
            .into(artWork)

        trackName.text = track?.trackName!!
        artistName.text = track.artistName
        trackDuration.text = trackTimeFormat(track.trackTimeMillis)
        trackAlbum.text = track.collectionName
        trackYear.text = track.getReleaseYear()
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        buttonPlayPause = findViewById(R.id.button_play_pause)
        trackPlayer = TrackPlayer(this, mediaPlayer, url, buttonPlayPause, trackTimer)
        trackPlayer.preparePlayer()
        buttonPlayPause.setOnClickListener { trackPlayer.playbackControl() }
    }

    override fun onPause() {
        super.onPause()
        trackPlayer.pausePlayer()
        trackPlayer.stopUpdater()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        trackPlayer.stopUpdater()
    }

    companion object{
        const val TRACK_ID = "TRACK_ID"
    }
}


