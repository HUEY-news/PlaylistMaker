package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
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
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.dto.SearchResponse
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.presentation.player.PlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    lateinit var activitySearchBinding: ActivitySearchBinding
    private val creator = Creator()

    private lateinit var placeholderIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var searchHistoryTrackList: RecyclerView
    private lateinit var searchHistoryButton: Button
    private lateinit var searchTrackAdapter: SearchTrackAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchHistory: SearchHistory
    private lateinit var progressBar: ProgressBar

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        activitySearchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(activitySearchBinding.root)

        placeholderIcon = findViewById(R.id.placeholderIcon)
        placeholderText = findViewById(R.id.placeholderText)
        placeholderButton = findViewById(R.id.placeholderButton)
        searchHistoryContainer = findViewById(R.id.searchHistoryContainer)
        searchHistoryTrackList = findViewById(R.id.searchHistoryTrackList)
        searchHistoryButton = findViewById(R.id.searchHistoryButton)
        progressBar = findViewById(R.id.progressBar)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }

        searchTrackAdapter = SearchTrackAdapter {track ->
            if (clickDebounce()) {

                // ДОБАВЛЯЕТ ТРЕК В ИСТОРИЮ ПОИСКА
                val searchHistory = SearchHistory(App.sharedPreferences)
                searchHistory.addTrackToHistory(track)

                // ЗАПУСКАЕТ PLAYER ACTIVITY И ПЕРЕДАЁТ ТРЕК
                val intent = Intent(this, PlayerActivity::class.java)
                intent.putExtra(PlayerActivity.TRACK_ID, track)
                startActivity(intent)
            }
        }
        activitySearchBinding.searchTrackList.adapter = searchTrackAdapter
        searchHistoryAdapter = SearchHistoryAdapter(arrayListOf())
        searchHistoryTrackList.adapter = searchHistoryAdapter

        searchHistory = SearchHistory(App.sharedPreferences)

        if (searchHistory.getHistory().isNotEmpty()) {
            searchHistoryAdapter.setTracks(searchHistory.getHistory())
            searchHistoryContainer.isVisible = true
        }

        searchHistoryButton.setOnClickListener {
            searchHistoryContainer.isVisible = false
            searchHistory.clearHistory() }

        // отслеживание состояния фокуса поля ввода:
        activitySearchBinding.searchField.setOnFocusChangeListener { view, hasFocus ->
            if (searchHistory.getHistory().isNotEmpty()){
                searchHistoryAdapter.setTracks(searchHistory.getHistory())
                searchHistoryContainer.isVisible =
                    if (hasFocus && activitySearchBinding.searchField.text.isEmpty()) {
                        true
                    } else {
                        false
                    }
            }
        }

        placeholderButton.setOnClickListener {
            searchRequest(activitySearchBinding.searchField.text.toString()) }

        activitySearchBinding.resetButton.setOnClickListener {
            activitySearchBinding.searchField.setText("")
            clearTrackList()

            // спрятать виртуальную клавиатуру:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(activitySearchBinding.searchField.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {

            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {

                if (string.isNullOrEmpty()) hidePlaceholder()

                activitySearchBinding.resetButton.isVisible =
                    if (string.isNullOrEmpty()) {
                        false
                    } else {
                        true
                    }

                clearTrackList()
                searchDebounce()

                if (searchHistory.getHistory().isNotEmpty()){
                    searchHistoryAdapter.setTracks(searchHistory.getHistory())
                    searchHistoryContainer.isVisible =
                        if (activitySearchBinding.searchField.hasFocus() && string?.isEmpty() == true) {
                            true
                        } else {
                            false
                        }
                }
            }
        }
        activitySearchBinding.searchField.addTextChangedListener(textWatcher)
    }

    private val searchRunnable = Runnable {
        searchRequest(activitySearchBinding.searchField.text.toString()) }
    private val handler = Handler(Looper.getMainLooper())

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(request: String)
    {
        if (request.isNotEmpty()) {
            hidePlaceholder()
            clearTrackList()
            progressBar.isVisible = true

            creator.provideApiService().search(request).enqueue(object : Callback<SearchResponse> {

                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        progressBar.visibility = View.GONE
                        if (response.code() == 200) {
                            val result = response.body()?.results
                            if (result?.isNotEmpty() == true) {
                                searchTrackAdapter.setItems(result)
                                activitySearchBinding.searchTrackList.isVisible = true
                            } else {
                                showPlaceholder(resources.getString(R.string.placeholder_empty_error))
                            }
                        } else {
                            showPlaceholder(resources.getString(R.string.placeholder_internet_error))
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        progressBar.isVisible = false
                        showPlaceholder(resources.getString(R.string.placeholder_internet_error))
                    }
                }
            )
        } else { clearTrackList() }
    }

    fun showPlaceholder(text: String) {
        clearTrackList()
        hideRecycler()

        when (text) {
            resources.getString(R.string.placeholder_empty_error) -> {
                placeholderIcon.setImageDrawable(getAttribute(R.attr.placeholderEmptyError))
                placeholderText.setText(R.string.placeholder_empty_error)
            }
            resources.getString(R.string.placeholder_internet_error) -> {
                placeholderIcon.setImageDrawable(getAttribute(R.attr.placeholderInternetError))
                placeholderText.setText(R.string.placeholder_internet_error)
                placeholderButton.isVisible = true
            }
        }
    }

    private fun hidePlaceholder(){
        placeholderIcon.setImageDrawable(null)
        placeholderText.text = null
        placeholderButton.isVisible = false
    }

    private fun hideRecycler(){
        activitySearchBinding.searchTrackList.visibility = View.GONE
    }

    private fun clearTrackList() {
        searchTrackAdapter.setItems(arrayListOf())
    }

    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = theme.obtainStyledAttributes(attrs)
        val placeholderResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(this, placeholderResourceId)
    }

    private var isClickAllowed = true
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}