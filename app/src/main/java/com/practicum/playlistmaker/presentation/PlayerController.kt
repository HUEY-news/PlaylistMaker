package com.practicum.playlistmaker.presentation

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
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
import com.practicum.playlistmaker.ui.player.PlayerActivity
import com.practicum.playlistmaker.utils.convertPixel
import com.practicum.playlistmaker.utils.convertTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerController(private val activity: Activity) {

    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val player: PlayerInteractor = Creator.providePlayerInteractor()
    private var state = PlayerState.DEFAULT

    private lateinit var  mainThreadHandler: Handler
    private lateinit var  timerUpdateRunnable: Runnable


    fun onCreate() {
        _binding = ActivityPlayerBinding.inflate(activity.layoutInflater)
        activity.setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())
        timerUpdateRunnable = updateTimer(binding.textViewTrackTimer)

        binding.buttonBack.setOnClickListener { activity.finish() }

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activity.intent.getParcelableExtra(TRACK_ID, Track::class.java)
        } else { // IF VERSION.SDK_INT < TIRAMISU
            activity.intent.getParcelableExtra(TRACK_ID)
        }

        fun render(track: Track) {
            with(binding) {
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
            .with(activity)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(convertPixel(4f, activity)))
            .into(binding.imageViewArtwork512)

        player.preparePlayer(track as Track)
        binding.buttonPlayPause.setOnClickListener {
            player.playbackControl()
        }

        (activity as PlayerActivity).lifecycleScope.launch(Dispatchers.IO){
            player.getPlayerState().collect { state ->
                this@PlayerController.state = state
                mainThreadHandler.post { checkPlayerState(state) }
            }
        }
    }


    fun onPause() {
        player.onPause()
        stopUpdater()
    }


    fun onDestroy() {
        player.onDestroy()
        stopUpdater()
    }


    private fun checkPlayerState(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> {
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPause))
                mainThreadHandler.post(timerUpdateRunnable)
            }
            PlayerState.PREPARED -> {
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
                stopUpdater()
                binding.textViewTrackTimer.text = ZERO_CONDITION
            }
            PlayerState.PAUSED -> {
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
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
        val typedArray = activity.theme.obtainStyledAttributes(attrs)
        val drawableResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(activity, drawableResourceId)
    }


    companion object{
        const val TRACK_ID = "TRACK_ID"

        private const val ZERO_CONDITION = "00:00"
        private const val DELAY = 500L
    }
}