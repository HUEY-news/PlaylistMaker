package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.presentation.player.PlayerActivity
import com.practicum.playlistmaker.presentation.track.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private val searchHistoryTrackList: ArrayList<Track> = arrayListOf()
    private var searchResultAdapter: TrackAdapter? = null
    private var searchHistoryAdapter: TrackAdapter? = null

    private var watcher: TextWatcher? = null

    private lateinit var errorPlaceholderText: String
    private lateinit var errorEmptyText: String
    private lateinit var errorInternetText: String
    private lateinit var errorServerText: String
    private var placeholderEmptyError: Int = 0
    private var placeholderInternetError: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        errorPlaceholderText = resources.getString(R.string.error_placeholder_text)
        errorEmptyText = resources.getString(R.string.error_empty_text)
        errorInternetText = resources.getString(R.string.error_internet_text)
        errorServerText = resources.getString(R.string.error_server_text)
        placeholderEmptyError = R.drawable.empty_error_placeholder
        placeholderInternetError = R.drawable.internet_error_placeholder

        searchResultAdapter = TrackAdapter { track -> onClickDebounce(track) }
        binding.searchResultRecycler.adapter = searchResultAdapter

        searchHistoryAdapter = TrackAdapter { track -> onClickDebounce(track) }
        binding.searchHistoryLayout.searchHistoryRecycler.adapter = searchHistoryAdapter

        viewModel.getSearchHistoryLiveData().observe(viewLifecycleOwner) { trackList ->
            searchHistoryTrackList.clear()
            searchHistoryTrackList.addAll(trackList)
            updateSearchHistory(searchHistoryTrackList)
        }

        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) { searchState ->
            when (searchState) {
                is SearchState.Loading -> showLoading()
                is SearchState.Content -> showContent(searchState.data)
                is SearchState.Error -> showPlaceholder(searchState.message)
            }
        }

        binding.searchHistoryLayout.searchHistoryButton.setOnClickListener {
            binding.searchHistoryLayout.searchHistoryContainer.isVisible = false
            viewModel.clearHistory()
        }

        binding.searchField.setOnFocusChangeListener { _, hasFocus ->

            if (searchHistoryTrackList.isNotEmpty()) {
                updateSearchHistory(searchHistoryTrackList)
                showSearchHistory(hasFocus && binding.searchField.text.isEmpty())
            }
        }

        binding.placeholderLayout.placeholderButton.setOnClickListener {
            viewModel.searchTrack(binding.searchField.text.toString())
        }

        binding.resetButton.setOnClickListener {
            binding.searchField.setText("")
            updateSearchResult(listOf())
            val inputMethodManager = requireContext()
                .getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchField.windowToken,
                0
            )
        }

        watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrEmpty()) {
                    hidePlaceholder()
                    updateSearchResult(listOf())
                }
                binding.resetButton.isVisible = !text.isNullOrEmpty()
                viewModel.searchDebounce(text = text?.toString() ?: "")

                if (searchHistoryTrackList.isNotEmpty()) {
                    updateSearchHistory(searchHistoryTrackList)
                    showSearchHistory(binding.searchField.hasFocus() && text?.isEmpty() == true)
                }
            }
        }
        watcher?.let { watcher -> binding.searchField.addTextChangedListener(watcher) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getHistory()
    }

    private fun showContent(trackList: List<Track>) {
        showProgressBar(false)
        updateSearchResult(trackList)
        showSearchResult(true)
        hidePlaceholder()
    }

    private fun showLoading() {
        showProgressBar(true)
        updateSearchResult(listOf())
        showSearchResult(false)
        updateSearchHistory(listOf())
        showSearchHistory(false)
        hidePlaceholder()
    }

    private fun showPlaceholder(errorMessage: String) {
        showProgressBar(false)
        updateSearchResult(listOf())
        showSearchResult(false)
        updateSearchHistory(listOf())
        showSearchHistory(false)

        when (errorMessage) {
            errorEmptyText -> showEmptyPlaceholder(errorMessage)
            errorInternetText -> showErrorPlaceHolder(errorMessage)
            errorServerText -> showErrorPlaceHolder(errorMessage)
        }
    }

    private fun showEmptyPlaceholder(errorMessage: String) {
        binding.placeholderLayout.placeholderIcon.setImageResource(placeholderEmptyError)
        binding.placeholderLayout.placeholderText.text = errorMessage
        binding.placeholderLayout.placeholderButton.isVisible = false
    }

    private fun showErrorPlaceHolder(errorMessage: String) {
        binding.placeholderLayout.placeholderIcon.setImageResource(placeholderInternetError)
        val resultErrorMessage = errorPlaceholderText + errorMessage
        binding.placeholderLayout.placeholderText.text = resultErrorMessage
        binding.placeholderLayout.placeholderButton.isVisible = true
    }

    private fun hidePlaceholder() {
        binding.placeholderLayout.placeholderIcon.setImageDrawable(null)
        binding.placeholderLayout.placeholderText.text = null
        binding.placeholderLayout.placeholderButton.isVisible = false
    }

    private fun showProgressBar(isVisible: Boolean) { binding.progressBar.isVisible = isVisible }
    private fun updateSearchResult(trackList: List<Track>) { searchResultAdapter?.setItems(trackList) }
    private fun showSearchResult(isVisible: Boolean) { binding.searchResultRecycler.isVisible = isVisible }
    private fun updateSearchHistory(trackList: List<Track>) { searchHistoryAdapter?.setItems(trackList) }
    private fun showSearchHistory(isVisible: Boolean) { binding.searchHistoryLayout.searchHistoryContainer.isVisible = isVisible }

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
            viewModel.addTrackToHistory(track)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}