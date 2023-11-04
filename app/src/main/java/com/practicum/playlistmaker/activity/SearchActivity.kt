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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.trackList.TrackListAdapter
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.databinding.LayoutPlaceholderBinding
import com.practicum.playlistmaker.databinding.LayoutSearchHistoryBinding
import com.practicum.playlistmaker.model.SearchResponse
import com.practicum.playlistmaker.network.AppleApiProvider
import com.practicum.playlistmaker.searchHistory.SearchHistory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var searchFieldContent: String? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val searchActivityBinding = ActivitySearchBinding.inflate(layoutInflater)
        val placeholderBinding = LayoutPlaceholderBinding.bind(searchActivityBinding.root)
        val searchHistoryBinding = LayoutSearchHistoryBinding.bind(searchActivityBinding.root)
        setContentView(searchActivityBinding.root)

//        searchActivityBinding.visibility.setOnClickListener {
//            searchHistoryBinding.searchHistoryContainer.isVisible = true
//            searchHistoryBinding.searchHistoryContainer.invalidate()
//            searchHistoryBinding.searchHistoryContainer.requestLayout()
//        }

        searchActivityBinding.backButton.setOnClickListener { finish() }
        val trackListAdapter = TrackListAdapter(arrayListOf())
        searchActivityBinding.trackList.adapter = trackListAdapter
        val sharedPreferences = App.sharedPreferences
        val searchHistory = SearchHistory(sharedPreferences)
        searchHistoryBinding.searchHistoryTrackList.adapter = searchHistory.adapter
        searchHistoryBinding.searchHistoryButton.setOnClickListener { searchHistory.clearHistory() }

        // TODO: разобраться с лишними методами:
        fun clearTrackList() {
            trackListAdapter.setTracks(arrayListOf())
        }

        // TODO: разобраться с лишними методами:
        fun showRecycler(){
            searchActivityBinding.trackList.visibility = View.VISIBLE
        }

        // TODO: разобраться с лишними методами:
        fun hideRecycler(){
            searchActivityBinding.trackList.visibility = View.GONE
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
                    placeholderBinding.placeholderIcon.setImageDrawable(getDrawable(R.attr.placeholderEmptyError))
                    placeholderBinding.placeholderText.setText(PLACEHOLDER_EMPTY_ERROR)
                }

                resources.getString(PLACEHOLDER_INTERNET_ERROR) -> {
                    placeholderBinding.placeholderIcon.setImageDrawable(getDrawable(R.attr.placeholderInternetError))
                    placeholderBinding.placeholderText.setText(PLACEHOLDER_INTERNET_ERROR)
                    placeholderBinding.placeholderButton.visibility = View.VISIBLE
                }
            }
        }

        fun hidePlaceholder(){
            placeholderBinding.placeholderIcon.setImageDrawable(null)
            placeholderBinding.placeholderText.text = null
            placeholderBinding.placeholderButton.visibility = View.GONE
        }

        // TODO: разобраться с лишними методами:
        fun hideContainer()
        {
            hideRecycler()
            hidePlaceholder()
        }

        val appleApiProvider = AppleApiProvider()

        fun query()
        {
            if (searchActivityBinding.searchField.text.isNotEmpty()) {
                appleApiProvider.api.search(searchActivityBinding.searchField.text.toString())
                    .enqueue(object : Callback<SearchResponse>
                    {
                        override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>)
                        {
                            if (response.code() == 200)
                            {
                                    Log.d("RESPONSE_CODE", response.code().toString())
                                    Log.d("RESPONSE_BODY", response.body()?.results.toString())

                                    hideContainer()

                                    if (response.body()?.results?.isNotEmpty() == true)
                                    {
                                        trackListAdapter.setTracks(response.body()?.results!!)
                                        showRecycler()
                                    }
                                    else
                                    {
                                        hideContainer()
                                        showPlaceholder(resources.getString(PLACEHOLDER_EMPTY_ERROR))
                                    }
                            }
                            else
                            {
                                Log.d("RESPONSE_CODE", response.code().toString())
                                hideContainer()
                                showPlaceholder(resources.getString(PLACEHOLDER_INTERNET_ERROR))
                            }
                        }

                        override fun onFailure(call: Call<SearchResponse>, t: Throwable)
                        {
                            Log.e("RESPONSE_ERROR", t.message.toString())
                            hideContainer()
                            showPlaceholder(resources.getString(PLACEHOLDER_INTERNET_ERROR))
                        }
                    })
            }else{ clearTrackList() }
        }

        // TODO: отслеживание состояния фокуса поля ввода:
        searchActivityBinding.searchField.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryBinding.searchHistoryHeader.visibility =
                if (hasFocus && searchActivityBinding.searchField.text.isEmpty()) View.VISIBLE else View.GONE
            searchHistoryBinding.searchHistoryTrackList.visibility =
                if (hasFocus && searchActivityBinding.searchField.text.isEmpty()) View.VISIBLE else View.GONE
            searchHistoryBinding.searchHistoryButton.visibility =
                if (hasFocus && searchActivityBinding.searchField.text.isEmpty()) View.VISIBLE else View.GONE
        }

        // TODO: отслеживание нажатие на кнопку "done":
        searchActivityBinding.searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                query()
                true
            }
            false
        }

        placeholderBinding.placeholderButton.setOnClickListener { query() }

        searchActivityBinding.resetButton.setOnClickListener {
            searchActivityBinding.searchField.setText("")
            // TODO: разобраться с лишними методами:
            clearTrackList()
            hideContainer()

            // todo: спрятать виртуальную клавиатуру:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchActivityBinding.searchField.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                searchActivityBinding.resetButton.visibility = resetButtonVisibility(string)
                if (string != null) searchFieldContent = string.toString()

                searchHistoryBinding.searchHistoryHeader.visibility =
                    if (searchActivityBinding.searchField.hasFocus() && string?.isEmpty() == true) View.VISIBLE else View.GONE
                searchHistoryBinding.searchHistoryTrackList.visibility
                    if (searchActivityBinding.searchField.hasFocus() && string?.isEmpty() == true) View.VISIBLE else View.GONE
                searchHistoryBinding.searchHistoryButton.visibility
                    if (searchActivityBinding.searchField.hasFocus() && string?.isEmpty() == true) View.VISIBLE else View.GONE
            }
        }
        searchActivityBinding.searchField.addTextChangedListener(textWatcher)
    }

    fun resetButtonVisibility(string: CharSequence?): Int {
        return if (string.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun onRestoreInstanceState(state: Bundle) {
        super.onRestoreInstanceState(state)
        searchFieldContent = state.getString(SEARCH_FIELD_CONTENT, "")
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putString(SEARCH_FIELD_CONTENT, searchFieldContent)
    }

    companion object {
        const val SEARCH_FIELD_CONTENT = "SEARCH_FIELD_CONTENT"
        const val PLACEHOLDER_EMPTY_ERROR = R.string.placeholder_empty_error
        const val PLACEHOLDER_INTERNET_ERROR = R.string.placeholder_internet_error
    }
}