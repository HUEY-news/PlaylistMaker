package com.practicum.playlistmaker.ui.player

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerState
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.utils.convertPixel
import com.practicum.playlistmaker.utils.convertTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var activityPlayerBinding : ActivityPlayerBinding

    private val player: PlayerInteractor = Creator.providePlayerInteractor()
    private var state = PlayerState.DEFAULT

    private lateinit var  mainThreadHandler: Handler
    private lateinit var  timerUpdateRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityPlayerBinding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(activityPlayerBinding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())
        timerUpdateRunnable = updateTimer(activityPlayerBinding.textViewTrackTimer)

        activityPlayerBinding.buttonBack.setOnClickListener { finish() }

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_ID, Track::class.java)
        } else { // IF VERSION.SDK_INT < TIRAMISU
            intent.getParcelableExtra(TRACK_ID)
        }

        fun render(track: Track) {
            with(activityPlayerBinding) {
                textViewTrackName.text = track.trackName
                textViewArtistName.text = track.artistName
                textViewTrackInfoDurationContent.text = convertTime(track.trackTimeMillis)
                textViewTrackInfoYearContent.text = track.collectionName
                textViewTrackInfoYearContent.text = track.getReleaseYear()
                textViewTrackInfoGenreContent.text = track.primaryGenreName
                textViewTrackInfoCountryContent.text = track.country
            }
        }

        if (track != null) render(track)
        else Log.e("HOUSTON_LOG_ERROR", "Вместо объекта класса Track из интента получен null")

        Glide
            .with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(convertPixel(4f, this)))
            .into(activityPlayerBinding.imageViewArtwork512)

        player.preparePlayer(track as Track)
        activityPlayerBinding.buttonPlayPause.setOnClickListener {
            player.playbackControl()
        }

        lifecycleScope.launch(Dispatchers.IO){
            player.getPlayerState().collect { state ->
                this@PlayerActivity.state = state
                mainThreadHandler.post { checkPlayerState(state) }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        player.onPause()
        stopUpdater()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.onDestroy()
        stopUpdater()
    }

    private fun checkPlayerState(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> {
                activityPlayerBinding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPause))
                mainThreadHandler.post(timerUpdateRunnable)
            }
            PlayerState.PREPARED -> {
                activityPlayerBinding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
                stopUpdater()
                activityPlayerBinding.textViewTrackTimer.text = ZERO_CONDITION
            }
            PlayerState.PAUSED -> {
                activityPlayerBinding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
            }
            else ->{}
        }
    }

    private fun updateTimer(trackTimer: TextView) : Runnable {
        return object : Runnable {
            override fun run() {
                if (state == PlayerState.PLAYING){
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
        private const val DELAY = 500L
    }
}
