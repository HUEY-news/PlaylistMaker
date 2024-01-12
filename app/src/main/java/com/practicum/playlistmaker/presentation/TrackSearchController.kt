package com.practicum.playlistmaker.presentation

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.ui.search.TrackAdapter

class TrackSearchController (
    private val activity: Activity,
    private val searchAdapter: TrackAdapter,
    private val historyAdapter: TrackAdapter,
    private val searchHistory: SearchHistory
) {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var emptyErrorText: String
    private lateinit var internetErrorText: String
    private var emptyErrorPlaceholder: Int = 0
    private var internetErrorPlaceholder: Int = 0

    private lateinit var placeholderIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var histotyRecycler: RecyclerView
    private lateinit var searchHistoryButton: Button
    private lateinit var progressBar: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest(binding.searchField.text.toString()) }
    private val trackInteractor = Creator.provideTrackInteractor()


    fun onCreate() {
        binding = ActivitySearchBinding.inflate(activity.layoutInflater)
        activity.setContentView(binding.root)

        emptyErrorText = activity.resources.getString(R.string.placeholder_empty_error)
        internetErrorText = activity.resources.getString(R.string.placeholder_internet_error)
        emptyErrorPlaceholder = R.attr.placeholderEmptyError
        internetErrorPlaceholder = R.attr.placeholderInternetError

        placeholderIcon = activity.findViewById(R.id.placeholderIcon)
        placeholderText = activity.findViewById(R.id.placeholderText)
        placeholderButton = activity.findViewById(R.id.placeholderButton)
        searchHistoryContainer = activity.findViewById(R.id.searchHistoryContainer)
        histotyRecycler = activity.findViewById(R.id.searchHistoryTrackList)
        searchHistoryButton = activity.findViewById(R.id.searchHistoryButton)
        progressBar = activity.findViewById(R.id.progressBar)

        activity.findViewById<ImageButton>(R.id.backButton).setOnClickListener { activity.finish() }

        binding.searchRecycler.adapter = searchAdapter
        histotyRecycler.adapter = historyAdapter

        if (searchHistory.getHistory().isNotEmpty()) {
            historyAdapter.setItems(searchHistory.getHistory())
            searchHistoryContainer.isVisible = true
        }

        // реакция на нажатие кнопки "очистить историю":
        searchHistoryButton.setOnClickListener {
            searchHistoryContainer.isVisible = false
            searchHistory.clearHistory()
        }

        // реализация отслеживания состояния фокуса поля поиска:
        binding.searchField.setOnFocusChangeListener { view, hasFocus ->
            if (searchHistory.getHistory().isNotEmpty()) {
                historyAdapter.setItems(searchHistory.getHistory())
                searchHistoryContainer.isVisible =
                    hasFocus && binding.searchField.text.isEmpty()
            }
        }

        // реакция на нажатие кнопки "обновить":
        placeholderButton.setOnClickListener {
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

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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
                    searchHistoryContainer.isVisible =
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
            progressBar.isVisible = true

            trackInteractor.searchTrack(request, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTrackList: List<Track>?) {
                    handler.post {
                        progressBar.visibility = View.GONE
                        if (foundTrackList != null) {
                            searchAdapter.setItems(foundTrackList)
                            binding.searchRecycler.isVisible = true
                            if (foundTrackList.isEmpty()) showPlaceholder(emptyErrorText)
                        } else showPlaceholder(internetErrorText)
                    }
                }
            })
        } else {
            clearTrackList()
        }
    }

    private fun showPlaceholder(text: String) {
        clearTrackList()
        hideRecycler()

        if (text == emptyErrorText) setEmptyErrorState()
        else if (text == internetErrorText) setInternetErrorState()
    }
    private fun setEmptyErrorState() {
        placeholderIcon.setImageDrawable(getAttribute(emptyErrorPlaceholder))
        placeholderText.text = emptyErrorText
        placeholderButton.isVisible = false
    }
    private fun setInternetErrorState() {
        placeholderIcon.setImageDrawable(getAttribute(internetErrorPlaceholder))
        placeholderText.text = internetErrorText
        placeholderButton.isVisible = true
    }
    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = activity.theme.obtainStyledAttributes(attrs)
        val placeholderResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(activity, placeholderResourceId)
    }

    private fun hidePlaceholder() {
        placeholderIcon.setImageDrawable(null)
        placeholderText.text = null
        placeholderButton.isVisible = false
    }

    private fun hideRecycler() {
        binding.searchRecycler.visibility = View.GONE
    }

    private fun clearTrackList() {
        searchAdapter.setItems(arrayListOf())
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}