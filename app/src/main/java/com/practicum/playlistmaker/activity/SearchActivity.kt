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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.trackList.TrackListAdapter
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
        setContentView(R.layout.activity_search)

        // TODO: реализовать activity_search через ViewBinding:
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val searchField = findViewById<EditText>(R.id.searchField)
        val resetButton = findViewById<ImageButton>(R.id.resetButton)
        val searchTrackList = findViewById<RecyclerView>(R.id.searchTrackList)
        // TODO: реализовать layout_placeholder через ViewBinding:
        val placeholderIcon = findViewById<ImageView>(R.id.placeholderIcon)
        val placeholderText = findViewById<TextView>(R.id.placeholderText)
        val placeholderButton = findViewById<Button>(R.id.placeholderButton)
        // TODO: реализовать layout_search_history через ViewBinding:
        val searchHistoryContainer = findViewById<LinearLayout>(R.id.searchHistoryContainer)
        val searchHistoryTrackList = findViewById<RecyclerView>(R.id.searchHistoryTrackList)
        val searchHistoryButton = findViewById<Button>(R.id.searchHistoryButton)

        backButton.setOnClickListener { finish() }
        val trackListAdapter = TrackListAdapter(arrayListOf())
        searchTrackList.adapter = trackListAdapter
        val sharedPreferences = App.sharedPreferences
        val searchHistory = SearchHistory(sharedPreferences)
        searchHistoryTrackList.adapter = searchHistory.adapter
        searchHistoryButton.setOnClickListener { searchHistory.clearHistory() }



        // TODO: разобраться с лишними методами:
        fun clearTrackList() {
            trackListAdapter.setTracks(arrayListOf())
        }

        // TODO: разобраться с лишними методами:
        fun showRecycler(){
            searchTrackList.visibility = View.VISIBLE
        }

        // TODO: разобраться с лишними методами:
        fun hideRecycler(){
            searchTrackList.visibility = View.GONE
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

        fun hidePlaceholder(){
            placeholderIcon.setImageDrawable(null)
            placeholderText.text = null
            placeholderButton.visibility = View.GONE
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
            if (searchField.text.isNotEmpty()) {
                appleApiProvider.api.search(searchField.text.toString())
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
        searchField.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryContainer.visibility =
                if (hasFocus && searchField.text.isEmpty()) View.VISIBLE else View.GONE
        }

        // TODO: отслеживание нажатие на кнопку "done":
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                query()
                true
            }
            false
        }

        placeholderButton.setOnClickListener { query() }

        resetButton.setOnClickListener {
            searchField.setText("")
            // TODO: разобраться с лишними методами:
            clearTrackList()
            hideContainer()

            // todo: спрятать виртуальную клавиатуру:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                resetButton.visibility = resetButtonVisibility(string)
                if (string != null) searchFieldContent = string.toString()

                searchHistoryContainer.visibility =
                    if (searchField.hasFocus() && string?.isEmpty() == true) View.VISIBLE else View.GONE
            }
        }
        searchField.addTextChangedListener(textWatcher)
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