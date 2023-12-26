package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.network.AppleApiProvider
import com.practicum.playlistmaker.data.network.dto.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var searchField: EditText
    private lateinit var resetButton: ImageButton
    private lateinit var trackRecycler: RecyclerView
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
        setContentView(R.layout.activity_search)

        searchField = findViewById(R.id.searchField)
        resetButton = findViewById(R.id.resetButton)
        trackRecycler = findViewById(R.id.searchTrackList)
        placeholderIcon = findViewById(R.id.placeholderIcon)
        placeholderText = findViewById(R.id.placeholderText)
        placeholderButton = findViewById(R.id.placeholderButton)
        searchHistoryContainer = findViewById(R.id.searchHistoryContainer)
        searchHistoryTrackList = findViewById(R.id.searchHistoryTrackList)
        searchHistoryButton = findViewById(R.id.searchHistoryButton)
        progressBar = findViewById(R.id.progressBar)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }

        searchTrackAdapter = SearchTrackAdapter(arrayListOf())
        trackRecycler.adapter = searchTrackAdapter
        searchHistoryAdapter = SearchHistoryAdapter(arrayListOf())
        searchHistoryTrackList.adapter = searchHistoryAdapter

        searchHistory = SearchHistory(App.sharedPreferences)
        searchHistoryButton.setOnClickListener {
            searchHistoryContainer.visibility = View.GONE
            searchHistory.clearHistory() }

        // ОТСЛЕЖИВАНИЕ СОСТОЯНИЯ ФОКУСА ПОЛЯ ВВОДА:
        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (searchHistory.getHistory().isNotEmpty()){
                searchHistoryAdapter.setTracks(searchHistory.getHistory())
                searchHistoryContainer.visibility =
                    if (hasFocus && searchField.text.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        placeholderButton.setOnClickListener { searchRequest(searchField.text.toString()) }

        resetButton.setOnClickListener {
            searchField.setText("")
            clearTrackList()

            // СПРЯТАТЬ ВИРТУАЛЬНУЮ КЛАВИАТУРУ:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                if (string.isNullOrEmpty()) hidePlaceholder()
                resetButton.visibility = if (string.isNullOrEmpty()) View.GONE else View.VISIBLE
                searchDebounce()

                if (searchHistory.getHistory().isNotEmpty()){
                    searchHistoryAdapter.setTracks(searchHistory.getHistory())
                    searchHistoryContainer.visibility =
                        if (searchField.hasFocus() && string?.isEmpty() == true) View.VISIBLE else View.GONE
                }
            }
        }
        searchField.addTextChangedListener(textWatcher)
    }

    private val searchRunnable = Runnable { searchRequest(searchField.text.toString()) }
    private val handler = Handler(Looper.getMainLooper())

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun searchRequest(request: String)
    {
        if (request.isNotEmpty()) {
            hidePlaceholder()
            clearTrackList()
            progressBar.visibility = View.VISIBLE

            AppleApiProvider.api.search(request).enqueue(object : Callback<SearchResponse> {

                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        progressBar.visibility = View.GONE
                        if (response.code() == 200) {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                // TODO: response.body()?.results нужно в отдельную переменную выносить,
                                //  чтобы не использовать оператор !!
                                searchTrackAdapter.setTracks(response.body()?.results!!)
                                trackRecycler.visibility = View.VISIBLE
                            } else {
                                showPlaceholder(resources.getString(PLACEHOLDER_EMPTY_ERROR))
                            }
                        } else {
                            showPlaceholder(resources.getString(PLACEHOLDER_INTERNET_ERROR))
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        showPlaceholder(resources.getString(PLACEHOLDER_INTERNET_ERROR))
                    }
                }
            )
        } else { clearTrackList() }
    }

    fun showPlaceholder(text: String) {
        clearTrackList()
        hideRecycler()

        when (text) {
            resources.getString(PLACEHOLDER_EMPTY_ERROR) -> {
                placeholderIcon.setImageDrawable(getAttribute(R.attr.placeholderEmptyError))
                placeholderText.setText(PLACEHOLDER_EMPTY_ERROR)
            }
            resources.getString(PLACEHOLDER_INTERNET_ERROR) -> {
                placeholderIcon.setImageDrawable(getAttribute(R.attr.placeholderInternetError))
                placeholderText.setText(PLACEHOLDER_INTERNET_ERROR)
                placeholderButton.visibility = View.VISIBLE
            }
        }
    }

    private fun hidePlaceholder(){
        placeholderIcon.setImageDrawable(null)
        placeholderText.text = null
        placeholderButton.visibility = View.GONE
    }

    private fun hideRecycler(){
        trackRecycler.visibility = View.GONE
    }

    private fun clearTrackList() {
        searchTrackAdapter.setTracks(arrayListOf())
    }

    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = theme.obtainStyledAttributes(attrs)
        val placeholderResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(this, placeholderResourceId)
    }

    companion object {

        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        const val PLACEHOLDER_EMPTY_ERROR = R.string.placeholder_empty_error
        const val PLACEHOLDER_INTERNET_ERROR = R.string.placeholder_internet_error
    }
}