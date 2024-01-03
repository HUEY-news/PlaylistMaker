package com.practicum.playlistmaker.presentation.player

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.utils.pixelConverter
import com.practicum.playlistmaker.utils.trackTimeFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlayerBinding

    private lateinit var  mainThreadHandler: Handler
    private lateinit var  timerUpdateRunnable: Runnable
    private lateinit var player: PlayerInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater).also {setContentView(it.root) }
        mainThreadHandler = Handler(Looper.getMainLooper())
        timerUpdateRunnable = updateTimer(binding.textViewTrackTimer)

        binding.buttonBack.setOnClickListener { finish() }

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
            .into(binding.imageViewArtwork512)

        binding.textViewTrackName.text = track?.trackName!!
        binding.textViewArtistName.text = track.artistName
        binding.textViewTrackInfoDurationContent.text = trackTimeFormat(track.trackTimeMillis)
        binding.textViewTrackInfoYearContent.text = track.collectionName
        binding.textViewTrackInfoYearContent.text = track.getReleaseYear()
        binding.textViewTrackInfoGenreContent.text = track.primaryGenreName
        binding.textViewTrackInfoCountryContent.text = track.country

        player = PlayerInteractorImpl()
        player.preparePlayer(track)
        binding.buttonPlayPause.setOnClickListener {
            player.playbackControl()
            checkPlayerState(player.getPlayerState())
        }
    }

    override fun onPause() {
        super.onPause()
        player.onPause()
        stopUpdater()
        checkPlayerState(player.getPlayerState())
    }

    override fun onDestroy() {
        super.onDestroy()
        player.onDestroy()
        stopUpdater()
    }

    private fun checkPlayerState(state: Int) {
        when (state) {
            STATE_PLAYING -> {
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPause))
                mainThreadHandler.post(timerUpdateRunnable)
            }
            STATE_PREPARED -> {
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
                stopUpdater()
                binding.textViewTrackTimer.text = ZERO_CONDITION
            }
            STATE_PAUSED -> {
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
            }
        }
    }

    private fun updateTimer(trackTimer: TextView) : Runnable {
        return object : Runnable {
            override fun run() {
                if (player.getPlayerState() == STATE_PLAYING){
                    trackTimer.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(player.getPlayerCurrentPosition())
                    mainThreadHandler.postDelayed(this, DELAY)
                }
            }
        }
    }

    private fun stopUpdater() {
        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
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

        private const val ZERO_CONDITION = "00:00"
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 500L
    }
}
