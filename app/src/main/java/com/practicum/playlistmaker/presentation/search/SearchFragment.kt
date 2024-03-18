package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.track.model.Track
import com.practicum.playlistmaker.presentation.player.PlayerActivity
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var searchAdapter: SearchAdapter? = null
    private var historyAdapter: SearchAdapter? = null
    private var watcher: TextWatcher? = null
    private lateinit var onClickDebounce: (Track) -> Unit

    private lateinit var errorText: String
    private lateinit var emptyErrorText: String
    private lateinit var internetErrorText: String
    private lateinit var serverErrorText: String
    private var emptyErrorPlaceholder: Int = 0
    private var internetErrorPlaceholder: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            viewModel.addTrackToHistory(track)
            val intent = Intent(requireContext(), PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.TRACK_ID, track)
            startActivity(intent)
        }

        searchAdapter = SearchAdapter { track -> onClickDebounce(track) }
        historyAdapter = SearchAdapter { track -> onClickDebounce(track) }

        val searchHistory: ArrayList<Track> = arrayListOf()

        viewModel.getSearchHistoryLiveData().observe(viewLifecycleOwner) { trackList ->
            searchHistory.clear()
            searchHistory.addAll(trackList)
        }

        viewModel.getSearchStateLiveData().observe(viewLifecycleOwner) { searchState ->
            when (searchState) {
                is SearchState.Loading -> showLoading()
                is SearchState.Content -> showContent(searchState.trackList)
                is SearchState.Error -> showPlaceholder(searchState.errorMessage)
            }
        }

        binding.searchRecycler.adapter = searchAdapter
        binding.layoutSearchHistory.historyRecycler.adapter = historyAdapter

        errorText = resources.getString(R.string.placeholder_error)
        emptyErrorText = resources.getString(R.string.placeholder_empty_error)
        internetErrorText = resources.getString(R.string.placeholder_internet_error)
        serverErrorText = resources.getString(R.string.placeholder_server_error)
        emptyErrorPlaceholder = R.drawable.empty_error_placeholder
        internetErrorPlaceholder = R.drawable.internet_error_placeholder

        if (searchHistory.isNotEmpty()) {
            historyAdapter?.setItems(searchHistory)
            binding.layoutSearchHistory.searchHistoryContainer.isVisible = true
        }

        // реакция на нажатие кнопки "очистить историю":
        binding.layoutSearchHistory.searchHistoryButton.setOnClickListener {
            binding.layoutSearchHistory.searchHistoryContainer.isVisible = false
            viewModel.clearHistory()
        }

        // реализация отслеживания состояния фокуса поля поиска:
        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            if (searchHistory.isNotEmpty()) {
                historyAdapter?.setItems(searchHistory)
                binding.layoutSearchHistory.searchHistoryContainer.isVisible =
                    hasFocus && binding.searchField.text.isEmpty()
            }
        }

        // реакция на нажатие кнопки "обновить":
        binding.layoutPlaceholder.placeholderButton.setOnClickListener {
            viewModel.searchTrack(binding.searchField.text.toString())
        }

        // реакция на нажатие кнопки сброса:
        binding.resetButton.setOnClickListener {
            binding.searchField.setText("")
            updateTrackList(listOf())
            // спрятать виртуальную клавиатуру:
            val inputMethodManager = requireContext()
                .getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchField.windowToken,
                0
            )
        }

        // реакция на изменение текста в поле поиска:
        watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.isNullOrEmpty()) {
                    hidePlaceholder()
                    updateTrackList(listOf())
                }
                binding.resetButton.isVisible = !text.isNullOrEmpty()
                viewModel.onSearchDebounce(text = text?.toString() ?: "")

                if (searchHistory.isNotEmpty()) {
                    historyAdapter?.setItems(searchHistory)
                    binding.layoutSearchHistory.searchHistoryContainer.isVisible =
                        binding.searchField.hasFocus() && text?.isEmpty() == true
                }
            }
        }
        watcher?.let { watcher -> binding.searchField.addTextChangedListener(watcher) }

        binding.searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.sendRequest(text = binding.searchField.text.toString())
            }
            false
        }
    }

    private fun showContent(trackList: List<Track>) {
        showProgressBar(false)
        updateTrackList(trackList)
        showRecycler(true)
    }

    private fun showLoading() {
        showProgressBar(true)
        hidePlaceholder()
        updateTrackList(listOf())
    }

    private fun showPlaceholder(errorMessage: String) {
        showProgressBar(false)
        updateTrackList(listOf())
        showRecycler(false)

        when (errorMessage) {
            emptyErrorText -> showEmpty(errorMessage)
            internetErrorText -> showError(errorMessage)
            serverErrorText -> showError(errorMessage)
        }
    }

    private fun showEmpty(errorMessage: String) {
        binding.layoutPlaceholder.placeholderIcon.setImageResource(emptyErrorPlaceholder)
        binding.layoutPlaceholder.placeholderText.text = errorMessage
        binding.layoutPlaceholder.placeholderButton.isVisible = false
    }

    private fun showError(errorMessage: String) {
        binding.layoutPlaceholder.placeholderIcon.setImageResource(internetErrorPlaceholder)
        val resultErrorMessage = errorText + errorMessage
        binding.layoutPlaceholder.placeholderText.text = resultErrorMessage
        binding.layoutPlaceholder.placeholderButton.isVisible = true
    }

    private fun hidePlaceholder() {
        binding.layoutPlaceholder.placeholderIcon.setImageDrawable(null)
        binding.layoutPlaceholder.placeholderText.text = null
        binding.layoutPlaceholder.placeholderButton.isVisible = false
    }

    private fun updateTrackList(trackList: List<Track>) {
        searchAdapter?.setItems(trackList)
    }

    private fun showRecycler(isVisible: Boolean) {
        binding.searchRecycler.isVisible = isVisible
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

}