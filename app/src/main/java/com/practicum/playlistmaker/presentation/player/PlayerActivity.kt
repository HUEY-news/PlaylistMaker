package com.practicum.playlistmaker.presentation.player

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.utils.pixelConverter
import com.practicum.playlistmaker.utils.trackTimeFormat

class PlayerActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityPlayerBinding
    private lateinit var player: PlayerInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)

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

        player = PlayerInteractor(
            trackTimer = binding.textViewTrackTimer)
        player.preparePlayer(track)
        binding.buttonPlayPause.setOnClickListener {
            checkButtonState(player.getPlayerState())
            player.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        checkButtonState(player.getPlayerState())
        player.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        checkButtonState(player.getPlayerState())
        player.onDestroy()
    }

    private fun checkButtonState(state: Int) {
        when (state) {
            STATE_PLAYING ->
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPause))
            STATE_PREPARED, STATE_PAUSED ->
                binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
        }
    }

    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = applicationContext.theme.obtainStyledAttributes(attrs)
        val drawableResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(applicationContext, drawableResourceId)
    }

    companion object{
        const val TRACK_ID = "TRACK_ID"

        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}

