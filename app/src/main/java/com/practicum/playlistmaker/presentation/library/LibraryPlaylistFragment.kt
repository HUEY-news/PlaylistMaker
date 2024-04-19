package com.practicum.playlistmaker.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistLibraryBinding
import com.practicum.playlistmaker.domain.library.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistLibraryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<LibraryPlaylistViewModel>()

    private var libraryPlaylistAdapter: LibraryPlaylistAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        libraryPlaylistAdapter = LibraryPlaylistAdapter()
        binding.playlistRecycler.adapter = libraryPlaylistAdapter

        viewModel.observeCurrentState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistPageState.Content -> showContent(state.data)
                is PlaylistPageState.Empty -> showEmpty()
            }
        }

        binding.buttonNewPlaylist.setOnClickListener {
            requireParentFragment().findNavController().navigate(R.id.action_libraryFragment_to_libraryNewPlaylistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        libraryPlaylistAdapter?.setItems(playlistList)
    }
    private fun showPlaylistRecycler(isVisible: Boolean) {
        binding.playlistRecycler.isVisible = isVisible
    }
    private fun showEmptyPlaceholder(isVisible: Boolean) {
        binding.placeholderContainer.isVisible = isVisible
    }
}