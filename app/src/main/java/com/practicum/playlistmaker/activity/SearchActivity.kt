package com.practicum.playlistmaker.activity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.model.Debouncer
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
    private lateinit var trackRecycler: RecyclerView
    private lateinit var placeholderIcon: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var searchHistoryTrackList: RecyclerView
    private lateinit var searchHistoryButton: Button
    private lateinit var trackListAdapter: TrackListAdapter
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

        trackListAdapter = TrackListAdapter(arrayListOf())
        trackRecycler.adapter = trackListAdapter
        searchHistoryAdapter = SearchHistoryAdapter(arrayListOf())
        searchHistoryTrackList.adapter = searchHistoryAdapter

        val debouncer = Debouncer(this@SearchActivity)

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

        placeholderButton.setOnClickListener { searchRequest() }

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
                debouncer.searchDebounce()

                if (searchHistory.getHistory().isNotEmpty()){
                    searchHistoryAdapter.setTracks(searchHistory.getHistory())
                    searchHistoryContainer.visibility =
                        if (searchField.hasFocus() && string?.isEmpty() == true) View.VISIBLE else View.GONE
                }
            }
        }
        searchField.addTextChangedListener(textWatcher)
    }

    // TODO: Текст для поиска лучше передавать параметром в эту функцию
    fun searchRequest()
    {
        if (searchField.text.isNotEmpty()) {
            hidePlaceholder()
            clearTrackList()
            progressBar.visibility = View.VISIBLE
            // TODO: А зачем на каждый запрос создавать новый объект AppleApiProvider?
            //  Можно сделать синглтон
            AppleApiProvider().api.search(searchField.text.toString()).enqueue(object : Callback<SearchResponse> {

                    override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                        progressBar.visibility = View.GONE
                        if (response.code() == 200) {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                // TODO: response.body()?.results нужно в отдельную переменную выносить,
                                //  чтобы не использовать оператор !!
                                trackListAdapter.setTracks(response.body()?.results!!)
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

        // TODO: Зачем нужно объявлять функцию внутри другой функции?
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
        trackRecycler.visibility = View.GONE
    }

    private fun clearTrackList() {
        trackListAdapter.setTracks(arrayListOf())
    }

    companion object {
        const val PLACEHOLDER_EMPTY_ERROR = R.string.placeholder_empty_error
        const val PLACEHOLDER_INTERNET_ERROR = R.string.placeholder_internet_error
    }
}