package com.practicum.playlistmaker.ui.player

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.presentation.player.PlayerView
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.util.convertPixel
import com.practicum.playlistmaker.util.convertTime

class PlayerActivity : AppCompatActivity(), PlayerView {

    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerPresenter = Creator.providePlayerPresenter (
        this,
        lifecycleScope
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        val track: Track? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRACK_ID, Track::class.java)
        } else { // IF VERSION.SDK_INT < TIRAMISU
            intent.getParcelableExtra(TRACK_ID)
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
            .with(this)
            .load(track?.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(convertPixel(4f, this)))
            .into(binding.imageViewArtwork512)

        playerPresenter.preparePlayer(track)
        binding.buttonPlayPause.setOnClickListener { playerPresenter.playbackControl() }
        playerPresenter.onCreate()
    }

    override fun onPause() {
        super.onPause()
        playerPresenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerPresenter.onDestroy()

    }

    override fun setPlayImage() {
        binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
    }
    override fun setPauseImage() {
        binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPause))
    }
    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = theme.obtainStyledAttributes(attrs)
        val drawableResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(this, drawableResourceId)
    }

    override fun setTimer(time: String) {
        binding.textViewTrackTimer.text = time
    }
    override fun resetTimer() {
        binding.textViewTrackTimer.text = ZERO_CONDITION
    }
    companion object{
        const val TRACK_ID = "TRACK_ID"
        private const val ZERO_CONDITION = "00:00"
    }
}
