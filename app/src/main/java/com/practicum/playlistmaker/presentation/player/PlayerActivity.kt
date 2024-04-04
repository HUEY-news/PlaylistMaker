package com.practicum.playlistmaker.presentation.player

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.player.PlayerState
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

    private var playButtonImage: Int = 0
    private var pauseButtonImage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playButtonImage = R.drawable.button_play_image
        pauseButtonImage = R.drawable.button_pause_image

        if (!viewModel.observeCurrentTrack().isInitialized) {
            viewModel.initPlayer(getTrack())
        }

        viewModel.observeCurrentTrack().observe(this) { track ->
            updateUI(track)
        }

        viewModel.observePlayerState().observe(this) { playerState ->
            binding.buttonPlay.isEnabled = playerState.isPlayButtonEnabled
            binding.textViewTrackTimer.text = playerState.progress
            when (playerState) {
                is PlayerState.Default, is PlayerState.Prepared, is PlayerState.Paused -> showPlayButton()
                is PlayerState.Playing -> showPauseButton()
            }
        }

        viewModel.observeFavorite().observe(this) { isFavorite ->
            updateFavoriteButton(isFavorite)
        }

        binding.buttonBack.setOnClickListener { finish() }
        binding.buttonPlay.setOnClickListener { viewModel.onPlayButtonClicked() }
        binding.buttonFavorite.setOnClickListener { viewModel.onFavoriteClicked() }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun getTrack(): Track? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getParcelableExtra(TRACK_ID, Track::class.java)
        } else return intent.getParcelableExtra(TRACK_ID)
    }

    private fun showPlayButton() { binding.buttonPlay.setImageResource(playButtonImage) }
    private fun showPauseButton() { binding.buttonPlay.setImageResource(pauseButtonImage)}

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) binding.buttonFavorite.setImageResource(R.drawable.button_favorite_enabled_image)
        else binding.buttonFavorite.setImageResource(R.drawable.button_favorite_disabled_image)
    }

    private fun updateUI(track: Track) {
        with (binding) {
            textViewTrackName.text = track.trackName
            textViewArtistName.text = track.artistName
            textViewTrackInfoDurationContent.text = convertTime(track.trackTimeMillis)
            textViewTrackInfoYearContent.text = track.collectionName
            textViewTrackInfoYearContent.text = convertDate(track.releaseDate)
            textViewTrackInfoGenreContent.text = track.primaryGenreName
            textViewTrackInfoCountryContent.text = track.country
            updateFavoriteButton(track.isFavorite)
        }

        Glide
            .with(applicationContext)
            .load(convertArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(convertPixel(4f, this)))
            .into(binding.imageViewArtwork512)
    }

    companion object {
        const val TRACK_ID = "TRACK_ID"
    }
}