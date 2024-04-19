package com.practicum.playlistmaker.presentation.library

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.library.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<LibraryPlaylistViewModel>()

    private var playlistAdapter: PlaylistAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter()
        binding.playlistRecycler.adapter = playlistAdapter

        viewModel.observeCurrentState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.Content -> showContent(state.data)
                is PlaylistState.Empty -> showEmpty()
            }
        }

        binding.buttonNewPlaylist.setOnClickListener {
            requireParentFragment().findNavController().navigate(R.id.action_create_new_playlist)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun showContent(playlistList: List<Playlist>) {
        showEmptyPlaceholder(false)
        updateLibrary(playlistList)
        showPlaylistRecycler(true)
    }

    private fun showEmpty() {
        showEmptyPlaceholder(true)
        updateLibrary(listOf())
        showPlaylistRecycler(false)
    }

    private fun updateLibrary(playlistList: List<Playlist>) {
        playlistAdapter?.setItems(playlistList)
        Log.d("TEST", "UI: updateLibrary($playlistList)")
    }
    private fun showPlaylistRecycler(isVisible: Boolean) {
        binding.playlistRecycler.isVisible = isVisible
        Log.d("TEST", "UI: showPlaylistRecycler($isVisible)")
    }
    private fun showEmptyPlaceholder(isVisible: Boolean) {
        binding.placeholderContainer.isVisible = isVisible
        Log.d("TEST", "UI: showEmptyPlaceholder($isVisible)")
    }
}