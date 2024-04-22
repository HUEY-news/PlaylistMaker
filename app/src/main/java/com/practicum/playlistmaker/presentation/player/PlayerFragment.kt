package com.practicum.playlistmaker.presentation.player

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.player.PlayerState
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.util.convertArtwork
import com.practicum.playlistmaker.util.convertDate
import com.practicum.playlistmaker.util.convertPixel
import com.practicum.playlistmaker.util.convertTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlayerViewModel>()

    private var playlistAdapter: PlayerPlaylistAdapter? = null

    private lateinit var track: Track
    private var playButtonImage: Int = 0
    private var pauseButtonImage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) it.getParcelable(TRACK_ID, Track::class.java)!!
            else it.getParcelable(TRACK_ID)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playButtonImage = R.drawable.button_play_image
        pauseButtonImage = R.drawable.button_pause_image

        playlistAdapter = PlayerPlaylistAdapter { playlist ->
            viewModel.addTrackToPlaylist(
                track = track,
                playlist = playlist
            )
        }

        binding.bottomSheetRecycler.adapter = playlistAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(
            object: BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.isVisible = false
                        }
                        else -> {
                            binding.overlay.isVisible = true
                            viewModel.getAllPlaylistsFromLibrary()
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset + 1
                }
            })

        if (!viewModel.observeCurrentTrack().isInitialized) { viewModel.initPlayer(track) }
        viewModel.observeCurrentTrack().observe(viewLifecycleOwner) { track -> updateUI(track) }
        viewModel.observeFavorite().observe(viewLifecycleOwner) { isFavorite -> updateFavoriteButton(isFavorite) }

        viewModel.observePlayerState().observe(viewLifecycleOwner) { playerState ->
            binding.buttonPlay.isEnabled = playerState.isPlayButtonEnabled
            binding.textViewTrackTimer.text = playerState.progress
            when (playerState) {
                is PlayerState.Default, is PlayerState.Prepared, is PlayerState.Paused -> showPlayButton()
                is PlayerState.Playing -> showPauseButton()
            }
        }

        viewModel.observePlaylistCollection().observe(viewLifecycleOwner) { itemList ->
            updatePlaylistCollection(itemList)
        }

        viewModel.observeTrackAddingStatus().observe(viewLifecycleOwner) { status ->
            when (status) {
                is TrackAddingStatus.Done -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    showToast(status.playlist, false)
                }
                is TrackAddingStatus.NotDone -> {
                    showToast(status.playlist, true)
                }
                is TrackAddingStatus.Ready -> {}
            }
        }

        binding.buttonPlay.setOnClickListener { viewModel.onPlayButtonClicked() }
        binding.buttonFavorite.setOnClickListener { viewModel.onFavoriteClicked() }
        binding.buttonAdd.setOnClickListener{ bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED }
        binding.bottomSheetButton.setOnClickListener { findNavController().navigate(R.id.action_playerFragment_to_libraryNewPlaylistFragment) }
        binding.buttonBack.setOnClickListener { findNavController().navigateUp() }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else findNavController().navigateUp()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun showPlayButton() { binding.buttonPlay.setImageResource(playButtonImage) }
    private fun showPauseButton() { binding.buttonPlay.setImageResource(pauseButtonImage) }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) binding.buttonFavorite.setImageResource(R.drawable.button_favorite_enabled_image)
        else binding.buttonFavorite.setImageResource(R.drawable.button_favorite_disabled_image)
    }

    private fun updateUI(track: Track) {
        with(binding) {
            textViewTrackName.text = track.trackName
            textViewArtistName.text = track.artistName
            textViewTrackInfoDurationContent.text = convertTime(track.trackTimeMillis)
            textViewTrackInfoAlbumContent.text = track.collectionName
            textViewTrackInfoYearContent.text = convertDate(track.releaseDate)
            textViewTrackInfoGenreContent.text = track.primaryGenreName
            textViewTrackInfoCountryContent.text = track.country
        }

        updateFavoriteButton(track.isFavorite)

        Glide
            .with(requireContext())
            .load(convertArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .transform(RoundedCorners(convertPixel(4f, requireContext())))
            .into(binding.imageViewArtwork512)
    }

    private fun updatePlaylistCollection(itemList: List<Playlist>) { playlistAdapter?.setItems(itemList) }

    private fun showToast(playlist: Playlist, exists: Boolean) {
        if (exists) Toast.makeText(requireContext(), "Трек уже добавлен в плейлист [ ${playlist.playlistName} ]", Toast.LENGTH_SHORT).show()
        else Toast.makeText(requireContext(), "Добавлено в плейлист [ ${playlist.playlistName} ]", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TRACK_ID = "TRACK_ID"

        fun createBundle(track: Track) = Bundle().apply {
            putParcelable(TRACK_ID, track)
        }
    }
}