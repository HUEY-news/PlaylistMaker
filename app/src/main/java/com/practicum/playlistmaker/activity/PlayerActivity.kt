package com.practicum.playlistmaker.activity

import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.pixelConverter
import com.practicum.playlistmaker.trackTimeFormat

class PlayerActivity : AppCompatActivity() {

    private lateinit var buttonPlayPause: ImageButton
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    var url = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/ac/c7/d1/acc7d13f-6634-495f-caf6-491eccb505e8/mzaf_4002676889906514534.plus.aac.p.m4a"

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

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_ID, Track::class.java)
        } else { // IF VERSION.SDK_INT < TIRAMISU
            intent.getParcelableExtra(TRACK_ID)
        }

        Glide
            .with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(pixelConverter(4f, this)))
            .into(artWork)

        trackTime.text = trackTimeFormat(track?.trackTimeMillis!!)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text = trackTimeFormat(track.trackTimeMillis)
        trackAlbum.text = track.collectionName
        trackYear.text = track.getReleaseYear()
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        buttonPlayPause = findViewById(R.id.button_play_pause)
        preparePlayer()
        buttonPlayPause.setOnClickListener { playbackControl() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlayPause.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun playbackControl(){
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    companion object{
        const val TRACK_ID = "TRACK_ID"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}


