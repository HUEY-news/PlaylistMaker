package com.practicum.playlistmaker.presentation

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.ui.search.TrackAdapter
import com.practicum.playlistmaker.util.Creator

class SearchController (
    private val activity: Activity,
    private val searchAdapter: TrackAdapter,
    private val historyAdapter: TrackAdapter,
    private val searchHistory: SearchHistory
) {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var emptyErrorText: String
    private lateinit var internetErrorText: String
    private var emptyErrorPlaceholder: Int = 0
    private var internetErrorPlaceholder: Int = 0

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest(binding.searchField.text.toString()) }
    private val trackInteractor = Creator.provideTrackInteractor(activity)


    fun onCreate() {
        _binding = ActivitySearchBinding.inflate(activity.layoutInflater)
        activity.setContentView(binding.root)

        emptyErrorText = activity.resources.getString(R.string.placeholder_empty_error)
        internetErrorText = activity.resources.getString(R.string.placeholder_internet_error)
        emptyErrorPlaceholder = R.attr.placeholderEmptyError
        internetErrorPlaceholder = R.attr.placeholderInternetError

        activity.findViewById<ImageButton>(R.id.backButton).setOnClickListener { activity.finish() }

        binding.searchRecycler.adapter = searchAdapter
        binding.layoutSearchHistory.historyRecycler.adapter = historyAdapter

        if (searchHistory.getHistory().isNotEmpty()) {
            historyAdapter.setItems(searchHistory.getHistory())
            binding.layoutSearchHistory.searchHistoryContainer.isVisible = true
        }

        // реакция на нажатие кнопки "очистить историю":
        binding.layoutSearchHistory.searchHistoryButton.setOnClickListener {
            binding.layoutSearchHistory.searchHistoryContainer.isVisible = false
            searchHistory.clearHistory()
        }

        // реализация отслеживания состояния фокуса поля поиска:
        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            if (searchHistory.getHistory().isNotEmpty()) {
                historyAdapter.setItems(searchHistory.getHistory())
                binding.layoutSearchHistory.searchHistoryContainer.isVisible =
                    hasFocus && binding.searchField.text.isEmpty()
            }
        }

        // реакция на нажатие кнопки "обновить":
        binding.layoutPlaceholder.placeholderButton.setOnClickListener {
            searchRequest(binding.searchField.text.toString())
        }

        // реакция на нажатие кнопки сброса:
        binding.resetButton.setOnClickListener {
            binding.searchField.setText("")
            clearTrackList()

            // спрятать виртуальную клавиатуру:
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchField.windowToken,
                0
            )
        }

        // реакция на изменение текста в поле поиска:
        binding.searchField.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->

                if (charSequence.isNullOrEmpty()) hidePlaceholder()
                binding.resetButton.isVisible = !charSequence.isNullOrEmpty()
                clearTrackList()
                searchDebounce()

                if (searchHistory.getHistory().isNotEmpty()) {
                    historyAdapter.setItems(searchHistory.getHistory())
                    binding.layoutSearchHistory.searchHistoryContainer.isVisible =
                        binding.searchField.hasFocus() && charSequence?.isEmpty() == true
                }
            }
        )
    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }


    private fun searchRequest(request: String) {
        if (request.isNotEmpty()) {
            hidePlaceholder()
            clearTrackList()
            binding.progressBar.isVisible = true

            trackInteractor.searchTrack(request, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTrackList: List<Track>?, errorMessage: String?) {
                    handler.post {
                        binding.progressBar.isVisible = false
                        if (foundTrackList != null) {
                            searchAdapter.setItems(foundTrackList)
                            binding.searchRecycler.isVisible = true
                        }
                        if (errorMessage != null) {
                            showPlaceholder(internetErrorText)
                        } else if (foundTrackList?.isEmpty()!!) {
                            showPlaceholder(emptyErrorText)
                        }
                    }
                }
            })
        } else { clearTrackList() }
    }


    private fun showPlaceholder(text: String) {
        clearTrackList()
        hideRecycler()

        if (text == emptyErrorText) setEmptyErrorState()
        else if (text == internetErrorText) setInternetErrorState()
    }
    private fun setEmptyErrorState() {
        binding.layoutPlaceholder.placeholderIcon.setImageDrawable(getAttribute(emptyErrorPlaceholder))
        binding.layoutPlaceholder.placeholderText.text = emptyErrorText
        binding.layoutPlaceholder.placeholderButton.isVisible = false
    }
    private fun setInternetErrorState() {
        binding.layoutPlaceholder.placeholderIcon.setImageDrawable(getAttribute(internetErrorPlaceholder))
        binding.layoutPlaceholder.placeholderText.text = internetErrorText
        binding.layoutPlaceholder.placeholderButton.isVisible = true
    }
    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = activity.theme.obtainStyledAttributes(attrs)
        val placeholderResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(activity, placeholderResourceId)
    }


    private fun hidePlaceholder() {
        binding.layoutPlaceholder.placeholderIcon.setImageDrawable(null)
        binding.layoutPlaceholder.placeholderText.text = null
        binding.layoutPlaceholder.placeholderButton.isVisible = false
    }

    private fun hideRecycler() {
        binding.searchRecycler.visibility = View.GONE
    }

    private fun clearTrackList() {
        searchAdapter.setItems(arrayListOf())
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}