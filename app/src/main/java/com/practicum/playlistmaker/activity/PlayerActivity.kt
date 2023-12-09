package com.practicum.playlistmaker.activity

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.pixelConverter
import com.practicum.playlistmaker.trackTimeFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var mainThreadHandler: Handler
    private lateinit var buttonPlayPause: ImageButton
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var url: String? = null
    private val timerUpdateRunnable = Runnable { updateTimer() }
    private lateinit var trackTimer: TextView

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

        url = track?.previewUrl

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

        mainThreadHandler = Handler(Looper.getMainLooper())
        buttonPlayPause = findViewById(R.id.button_play_pause)
        preparePlayer()
        buttonPlayPause.setOnClickListener { playbackControl() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
    }

    private fun preparePlayer(){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlayPause.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(timerUpdateRunnable)
            trackTimer.text = ZERO_CONDITION
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
        buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPause))
        playerState = STATE_PLAYING
        mainThreadHandler.post(timerUpdateRunnable)
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
        buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
        playerState = STATE_PAUSED
    }

    private fun updateTimer () : Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING){
                    trackTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(mediaPlayer.currentPosition)
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }
    }

    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = theme.obtainStyledAttributes(attrs)
        val drawableResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(this, drawableResourceId)
    }

    companion object{
        const val TRACK_ID = "TRACK_ID"
        const val ZERO_CONDITION = "00:00"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 500L
    }
}


