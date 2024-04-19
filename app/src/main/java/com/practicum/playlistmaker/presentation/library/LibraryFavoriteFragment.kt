package com.practicum.playlistmaker.presentation.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavouriteBinding
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.presentation.player.PlayerActivity
import com.practicum.playlistmaker.presentation.track.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryFavoriteFragment: Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<LibraryFavoriteViewModel>()

    private var favoriteListAdapter: TrackAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListAdapter = TrackAdapter { track -> onClickDebounce(track) }
        binding.favoriteTrackList.adapter = favoriteListAdapter
        binding.favoriteTrackList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.observeCurrentState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritePageState.Content -> showContent(state.data)
                is FavoritePageState.Empty -> showEmpty()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun showContent(trackList: List<Track>) {
        showEmptyPlaceholder(false)
        updateLibrary(trackList)
        showTrackListRecycler(true)
    }

    private fun showEmpty() {
        showEmptyPlaceholder(true)
        updateLibrary(listOf())
        showTrackListRecycler(false)
    }

    private fun updateLibrary(trackList: List<Track>) { favoriteListAdapter?.setItems(trackList) }
    private fun showTrackListRecycler(isVisible: Boolean) { binding.favoriteTrackList.isVisible = isVisible }
    private fun showEmptyPlaceholder(isVisible: Boolean) { binding.placeholderContainer.isVisible = isVisible }

    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun onClickDebounce(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.TRACK_ID, track)
            startActivity(intent)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}