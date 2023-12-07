package com.practicum.playlistmaker.activity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.Debounce
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.trackList.TrackListAdapter
import com.practicum.playlistmaker.model.SearchResponse
import com.practicum.playlistmaker.network.AppleApiProvider
import com.practicum.playlistmaker.searchHistory.SearchHistory
import com.practicum.playlistmaker.searchHistory.SearchHistoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var searchField: EditText
    private lateinit var resetButton: ImageButton
    private lateinit var searchTrackList: RecyclerView

    private lateinit var placeholderIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button

    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var searchHistoryTrackList: RecyclerView
    private lateinit var searchHistoryButton: Button

    private lateinit var trackListAdapter: TrackListAdapter
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchHistory: SearchHistory

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_search)

        searchField = findViewById(R.id.searchField)
        resetButton = findViewById(R.id.resetButton)
        searchTrackList = findViewById(R.id.searchTrackList)

        placeholderIcon = findViewById(R.id.placeholderIcon)
        placeholderText = findViewById(R.id.placeholderText)
        placeholderButton = findViewById(R.id.placeholderButton)

        searchHistoryContainer = findViewById(R.id.searchHistoryContainer)
        searchHistoryTrackList = findViewById(R.id.searchHistoryTrackList)
        searchHistoryButton = findViewById(R.id.searchHistoryButton)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }

        trackListAdapter = TrackListAdapter(arrayListOf())
        searchTrackList.adapter = trackListAdapter
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

        // ОТСЛЕЖИВАНИЕ НАЖАТИЯ НА КНОПКУ "DONE":
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRequest()
                true
            }
            false
        }

        placeholderButton.setOnClickListener { searchRequest() }

        resetButton.setOnClickListener {
            searchField.setText("")
            clearTrackList()
            hideContainer()

            // СПРЯТАТЬ ВИРТУАЛЬНУЮ КЛАВИАТУРУ:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        fun resetButtonVisibility(string: CharSequence?): Int {
            return if (string.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                resetButton.visibility = resetButtonVisibility(string)

                Debounce(this@SearchActivity).searchDebounce()

                if (searchHistory.getHistory().isNotEmpty()){
                    searchHistoryAdapter.setTracks(searchHistory.getHistory())
                    searchHistoryContainer.visibility =
                        if (searchField.hasFocus() && string?.isEmpty() == true) View.VISIBLE else View.GONE
                }
            }
        }
        searchField.addTextChangedListener(textWatcher)
    }

    fun searchRequest()
    {
        if (searchField.text.isNotEmpty()) {
            AppleApiProvider().api.search(searchField.text.toString())
                .enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        if (response.code() == 200) {
                            Log.d("RESPONSE_CODE", response.code().toString())
                            Log.d("RESPONSE_BODY", response.body()?.results.toString())

                            hideContainer()

                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackListAdapter.setTracks(response.body()?.results!!)
                                searchTrackList.visibility = View.VISIBLE
                            } else {
                                hideContainer()
                                showPlaceholder(resources.getString(PLACEHOLDER_EMPTY_ERROR))
                            }
                        } else {
                            Log.d("RESPONSE_CODE", response.code().toString())
                            hideContainer()
                            showPlaceholder(resources.getString(PLACEHOLDER_INTERNET_ERROR))
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        Log.e("RESPONSE_ERROR", t.message.toString())
                        hideContainer()
                        showPlaceholder(resources.getString(PLACEHOLDER_INTERNET_ERROR))
                    }
                }
                )
        } else { clearTrackList() }
    }

    fun showPlaceholder(text: String) {
        clearTrackList()
        hideRecycler()

        fun getDrawable(attr: Int): Drawable? {
            val attrs = intArrayOf(attr)
            val typedArray = theme.obtainStyledAttributes(attrs)
            val placeholderResourceId = typedArray.getResourceId(0, 0)
            typedArray.recycle()

            return ContextCompat.getDrawable(this, placeholderResourceId)
        }

        when (text) {
            resources.getString(PLACEHOLDER_EMPTY_ERROR) -> {
                placeholderIcon.setImageDrawable(getDrawable(R.attr.placeholderEmptyError))
                placeholderText.setText(PLACEHOLDER_EMPTY_ERROR)
            }
            resources.getString(PLACEHOLDER_INTERNET_ERROR) -> {
                placeholderIcon.setImageDrawable(getDrawable(R.attr.placeholderInternetError))
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
        searchTrackList.visibility = View.GONE
    }

    fun hideContainer()
    {
        hideRecycler()
        hidePlaceholder()
    }

    private fun clearTrackList() {
        trackListAdapter.setTracks(arrayListOf())
    }

    companion object {
        const val PLACEHOLDER_EMPTY_ERROR = R.string.placeholder_empty_error
        const val PLACEHOLDER_INTERNET_ERROR = R.string.placeholder_internet_error
    }
}