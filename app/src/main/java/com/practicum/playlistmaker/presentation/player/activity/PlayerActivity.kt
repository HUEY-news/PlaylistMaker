package com.practicum.playlistmaker.presentation.player.activity

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.track.model.Track
import com.practicum.playlistmaker.presentation.player.model.PlayerScreenState
import com.practicum.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.presentation.player.view_model.PlayerViewModelFactory
import com.practicum.playlistmaker.util.convertArtwork
import com.practicum.playlistmaker.util.convertDate
import com.practicum.playlistmaker.util.convertPixel
import com.practicum.playlistmaker.util.convertTime

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TEST", "PlayerActivity СОЗДАНА")
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, PlayerViewModelFactory()).get(PlayerViewModel::class.java)
        viewModel.getPlayerStateLivedata().observe(this) { state ->
            when (state) {
                PlayerScreenState.Default -> {}
                PlayerScreenState.Paused -> setPlayImage()
                PlayerScreenState.Prepared -> {
                    setPlayImage()
                    resetTimer()
                    viewModel.stopUpdater()
                }
                is PlayerScreenState.Playing -> {
                    setPauseImage()
                    viewModel.startUpdater()
                    setTimer(state.time)
                }
            }
        }
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
                textViewTrackInfoYearContent.text = convertDate(track.releaseDate)
                textViewTrackInfoGenreContent.text = track.primaryGenreName
                textViewTrackInfoCountryContent.text = track.country
            }
        }

        if (track != null) render(track)
        else Log.e("HOUSTON_LOG_ERROR", "Вместо объекта класса Track из интента получен null")

        Glide
            .with(this)
            .load(convertArtwork(track?.artworkUrl100!!))
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(convertPixel(4f, this)))
            .into(binding.imageViewArtwork512)

        viewModel.preparePlayer(track)
        binding.buttonPlayPause.setOnClickListener { viewModel.playbackControl() }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TEST", "PlayerActivity УНИЧТОЖЕНА")
        viewModel.onDestroy()

    }

    private fun setPlayImage() {
        binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPlay))
    }
    private fun setPauseImage() {
        binding.buttonPlayPause.setImageDrawable(getAttribute(R.attr.buttonPause))
    }
    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = theme.obtainStyledAttributes(attrs)
        val drawableResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(this, drawableResourceId)
    }

    private fun setTimer(time: String) { binding.textViewTrackTimer.text = time }
    private fun resetTimer() { binding.textViewTrackTimer.text = ZERO_CONDITION }
    companion object {
        const val TRACK_ID = "TRACK_ID"
        private const val ZERO_CONDITION = "00:00"
    }
}
