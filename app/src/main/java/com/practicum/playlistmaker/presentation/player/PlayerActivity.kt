package com.practicum.playlistmaker.presentation.player

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.util.convertArtwork
import com.practicum.playlistmaker.util.convertDate
import com.practicum.playlistmaker.util.convertPixel
import com.practicum.playlistmaker.util.convertTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlayerViewModel>()

    private lateinit var track: Track

    private var playButtonImage: Int = 0
    private var pauseButtonImage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playButtonImage = R.drawable.button_play_image
        pauseButtonImage = R.drawable.button_pause_image

        viewModel.getPlayerStateLivedata().observe(this) { state ->
            when (state) {
                PlayerStateSealedInterface.Default -> {}
                PlayerStateSealedInterface.Paused -> showPlayButton()
                PlayerStateSealedInterface.Prepared -> {
                    showPlayButton()
                    resetTimer()
                    viewModel.stopUpdater()
                }
                is PlayerStateSealedInterface.Playing -> {
                    showPauseButton()
                    viewModel.startUpdater()
                    setTimer(state.time)
                }
            }
        }

        if (getTrack() != null) track = getTrack()!!
        with (binding) {
            textViewTrackName.text = track.trackName
            textViewArtistName.text = track.artistName
            textViewTrackInfoDurationContent.text = convertTime(track.trackTimeMillis)
            textViewTrackInfoYearContent.text = track.collectionName
            textViewTrackInfoYearContent.text = convertDate(track.releaseDate)
            textViewTrackInfoGenreContent.text = track.primaryGenreName
            textViewTrackInfoCountryContent.text = track.country
        }

        Glide
            .with(applicationContext)
            .load(convertArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(convertPixel(4f, this)))
            .into(binding.imageViewArtwork512)

        binding.buttonBack.setOnClickListener { finish() }
        binding.buttonPlay.setOnClickListener { viewModel.playbackControl() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onPrepare(track)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        viewModel.onReset()
        super.onDestroy()
    }

    private fun getTrack(): Track? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getParcelableExtra(TRACK_ID, Track::class.java)
        } else { // IF VERSION.SDK_INT < TIRAMISU
            return intent.getParcelableExtra(TRACK_ID)
        }
    }

    private fun showPlayButton() { binding.buttonPlay.setImageResource(playButtonImage) }
    private fun showPauseButton() { binding.buttonPlay.setImageResource(pauseButtonImage)}

    private fun setTimer(time: String) { binding.textViewTrackTimer.text = time }
    private fun resetTimer() { binding.textViewTrackTimer.text = ZERO_CONDITION }

    companion object {
        const val TRACK_ID = "TRACK_ID"
        private const val ZERO_CONDITION = "00:00"
    }
}
