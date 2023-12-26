package com.practicum.playlistmaker.presentation.player

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.practicum.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlayer (
    private val context: Context,
    private val mediaPlayer: MediaPlayer,
    private val url: String?,
    private val buttonPlayPause: ImageButton,
    private val trackTimer: TextView) {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerUpdateRunnable = updateTimer(trackTimer)
    private var playerState = STATE_DEFAULT


    fun preparePlayer(){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlayPause.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
            playerState = STATE_PREPARED
            stopUpdater()
            trackTimer.text = ZERO_CONDITION
        }
    }



    fun playbackControl(){
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

    fun pausePlayer(){
        mediaPlayer.pause()
        buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
        playerState = STATE_PAUSED
    }

    private fun updateTimer (trackTimer: TextView) : Runnable {
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

    fun stopUpdater () {
        mainThreadHandler.removeCallbacks(timerUpdateRunnable)
    }

    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = context.theme.obtainStyledAttributes(attrs)
        val drawableResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(context, drawableResourceId)
    }

    companion object {
        const val ZERO_CONDITION = "00:00"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 500L
    }
}