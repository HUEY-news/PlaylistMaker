package com.practicum.playlistmaker.presentation.library

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.presentation.player.PlayerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistFragment: Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModels()

    private lateinit var trackConfirmDialog: MaterialAlertDialogBuilder
    private lateinit var playlistConfirmDialog: MaterialAlertDialogBuilder
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMore: BottomSheetBehavior<LinearLayout>
    private lateinit var trackAdapter: PlaylistTrackAdapter
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

        trackAdapter = PlaylistTrackAdapter(
            onItemClick = { track ->
                val arguments = PlayerFragment.createBundle(track)
                findNavController().navigate(R.id.action_playlistFragment_to_playerFragment, arguments)
            },
            onLongItemClick = { track ->
                removeTrackDialog(track)
            }
        )

        binding.bottomSheetRecycler.adapter = trackAdapter
        binding.bottomSheetRecycler.setHasFixedSize(true)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout).apply { state = BottomSheetBehavior.STATE_COLLAPSED }
        bottomSheetBehaviorMore = BottomSheetBehavior.from(binding.bottomSheetLayoutMore).apply { state = BottomSheetBehavior.STATE_HIDDEN }

        binding.emptySpace.doOnLayout {
            bottomSheetBehavior.setPeekHeight(
                binding.playlistCoordinatorLayout.height - binding.playlistConstraintLayout.height,
                false
            )
            binding.playlistScrollView.layoutParams = CoordinatorLayout.LayoutParams(
                binding.playlistScrollView.width,
                binding.playlistCoordinatorLayout.height - bottomSheetBehavior.peekHeight
            )
        }

        bottomSheetBehaviorMore.addBottomSheetCallback(
            object: BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                        else -> binding.overlay.isVisible = true
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset + 1
                }
            })

        viewModel.getPlaylist(playlistId)
        viewModel.observeCurrentPlaylist().observe(viewLifecycleOwner) { playlist -> updatePlaylist(playlist) }
        viewModel.observeCurrentPlaylistTracks().observe(viewLifecycleOwner) { trackList -> updateTrackList(trackList) }
        viewModel.observeCurrentPlaylistInfo().observe(viewLifecycleOwner) { info -> updatePlaylistInfo(info) }
        viewModel.observeIfCurrentPlaylistHasContent().observe(viewLifecycleOwner) { hasContent -> updatePlaylistContent(hasContent) }
        viewModel.observeEmptyShareMessage().observe(viewLifecycleOwner) { isEmpty -> if (isEmpty) showEmptyShareMessage()}

        binding.shareTextButton.setOnClickListener { viewModel.sharePlaylist(playlistId) }
        binding.editTextButton.setOnClickListener {
            val arguments = LibraryNewPlaylistFragment.createBundle(playlistId)
            findNavController().navigate(R.id.action_playlistFragment_to_libraryNewPlaylistFragment, arguments)
        }
        binding.deleteTextButton.setOnClickListener {
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
            removePlaylistDialog()
        }

        binding.shareButton.setOnClickListener { viewModel.sharePlaylist(playlistId) }
        binding.menuButton.setOnClickListener { bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehaviorMore.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
                } else findNavController().navigateUp()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun updatePlaylist(playlist: Playlist) {
        with(binding) {
            playlistName.text = playlist.playlistName
            playlistDescription.text = playlist.playlistDescription

            Glide
                .with(requireContext())
                .load(playlist.playlistCoverUri)
                .placeholder(R.drawable.ic_placeholder_45)
                .into(playlistImage)

            includePlaylistPlayer.playlistName.text = playlist.playlistName

            Glide
                .with(requireContext())
                .load(playlist.playlistCoverUri)
                .placeholder(R.drawable.ic_placeholder_45)
                .into(includePlaylistPlayer.playlistImage)
        }
    }

    private fun updateTrackList(trackList: List<Track>) {
        trackAdapter.setItems(trackList)
    }

    private fun updatePlaylistInfo(info: Pair<String, String>) {
        val text = "${info.first} • ${info.second}"
        binding.playlistInfo.text = text
        binding.includePlaylistPlayer.numberOfTracks.text = info.second
    }

    private fun updatePlaylistContent(hasContent: Boolean) {
        if (hasContent) {
            binding.bottomSheetRecycler.isVisible = true
            binding.emptyListMessage.isVisible = false
        } else {
            binding.bottomSheetRecycler.isVisible = false
            binding.emptyListMessage.isVisible = true
        }
    }

    private fun showEmptyShareMessage() {
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
        Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которым можно поделиться", Toast.LENGTH_SHORT).show()
    }

    private fun removeTrackDialog(track: Track) {
        trackConfirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNeutralButton("Отмена") { _, _ -> }
            .setPositiveButton("Удалить") { _, _-> viewModel.removeTrackFromPlaylist(track, playlistId) }

        val dialog = trackConfirmDialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
    }

    private fun removePlaylistDialog() {
        playlistConfirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setNeutralButton("Нет") { _, _ -> }
            .setPositiveButton("Да") { _, _->
                viewModel.removePlaylistFromLibrary(playlistId)
                findNavController().navigateUp()}

        val dialog = playlistConfirmDialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YP_Blue))
    }

    companion object {
        private const val PLAYLIST_ID = "PLAYLIST_ID"

        fun createBundle(playlistId: Int) = Bundle().apply {
            putInt(PLAYLIST_ID, playlistId)
        }
    }
}