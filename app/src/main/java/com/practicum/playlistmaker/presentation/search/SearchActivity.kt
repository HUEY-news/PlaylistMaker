package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.domain.track.TrackInteractor
import com.practicum.playlistmaker.presentation.player.PlayerActivity

class SearchActivity : AppCompatActivity() {

    lateinit var activitySearchBinding: ActivitySearchBinding

    private val trackInteractor = Creator.provideTrackInteractor()
    private val searchHistory = SearchHistory(App.sharedPreferences)
    private val searchAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            searchHistory.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.TRACK_ID, track)
            startActivity(intent)
        }
    }
    private val historyAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.TRACK_ID, track)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        searchRequest(activitySearchBinding.searchField.text.toString())
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    private lateinit var emptyErrorText: String
    private lateinit var internetErrorText: String
    private var emptyErrorPlaceholder: Int = 0
    private var internetErrorPlaceholder: Int = 0

    private lateinit var placeholderIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var searchHistoryTrackList: RecyclerView
    private lateinit var searchHistoryButton: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)

        emptyErrorText = resources.getString(R.string.placeholder_empty_error)
        internetErrorText = resources.getString(R.string.placeholder_internet_error)
        emptyErrorPlaceholder = R.attr.placeholderEmptyError
        internetErrorPlaceholder = R.attr.placeholderInternetError

        placeholderIcon = findViewById(R.id.placeholderIcon)
        placeholderText = findViewById(R.id.placeholderText)
        placeholderButton = findViewById(R.id.placeholderButton)
        searchHistoryContainer = findViewById(R.id.searchHistoryContainer)
        searchHistoryTrackList = findViewById(R.id.searchHistoryTrackList)
        searchHistoryButton = findViewById(R.id.searchHistoryButton)
        progressBar = findViewById(R.id.progressBar)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }

        activitySearchBinding.searchRecycler.adapter = searchAdapter
        searchHistoryTrackList.adapter = historyAdapter

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
        activitySearchBinding.searchField.setOnFocusChangeListener { view, hasFocus ->
            if (searchHistory.getHistory().isNotEmpty()) {
                historyAdapter.setItems(searchHistory.getHistory())
                searchHistoryContainer.isVisible =
                    hasFocus && activitySearchBinding.searchField.text.isEmpty()
            }
        }

        // реакция на нажатие кнопки "обновить":
        placeholderButton.setOnClickListener {
            searchRequest(activitySearchBinding.searchField.text.toString())
        }

        // реакция на нажатие кнопки сброса:
        activitySearchBinding.resetButton.setOnClickListener {
            activitySearchBinding.searchField.setText("")
            clearTrackList()

            // спрятать виртуальную клавиатуру:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                activitySearchBinding.searchField.windowToken,
                0
            )
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
        // реакция на изменение текста в поле поиска:
        activitySearchBinding.searchField.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->

                if (charSequence.isNullOrEmpty()) hidePlaceholder()
                activitySearchBinding.resetButton.isVisible = !charSequence.isNullOrEmpty()
                clearTrackList()
                searchDebounce()

                if (searchHistory.getHistory().isNotEmpty()) {
                    historyAdapter.setItems(searchHistory.getHistory())
                    searchHistoryContainer.isVisible =
                        activitySearchBinding.searchField.hasFocus() && charSequence?.isEmpty() == true
                }
            }
        )
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
                            activitySearchBinding.searchRecycler.isVisible = true
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
        val typedArray = theme.obtainStyledAttributes(attrs)
        val placeholderResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(this, placeholderResourceId)
    }

    private fun hidePlaceholder() {
        placeholderIcon.setImageDrawable(null)
        placeholderText.text = null
        placeholderButton.isVisible = false
    }

    private fun hideRecycler() {
        activitySearchBinding.searchRecycler.visibility = View.GONE
    }

    private fun clearTrackList() {
        searchAdapter.setItems(arrayListOf())
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}