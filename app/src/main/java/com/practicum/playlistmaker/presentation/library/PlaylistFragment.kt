package com.practicum.playlistmaker.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.presentation.track.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMore: BottomSheetBehavior<LinearLayout>
    private lateinit var trackAdapter: TrackAdapter
    private var playlistId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { playlistId = it.getInt(PLAYLIST_ID) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPlaylist(playlistId)
        viewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) { playlist -> updateUI(playlist) }

        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehaviorMore.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
                } else findNavController().navigateUp()
            }
        })
    }

    private fun updateUI(playlist: Playlist) {
        with(binding) {
            playlistName.text = playlist.playlistName
            playlistDescription.text = playlist.playlistDescription
            playlistInfo.text = "${getDurationSum(playlist)} · ${playlist.numberOfTracks}"

        }

        Glide
            .with(requireContext())
            .load(playlist.playlistCoverUri)
            .placeholder(R.drawable.ic_placeholder_artwork_240)
            .into(binding.playlistImage)
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createBundle(playlistId: Int) = Bundle().apply {
            putInt(PLAYLIST_ID, playlistId)
        }
    }
}