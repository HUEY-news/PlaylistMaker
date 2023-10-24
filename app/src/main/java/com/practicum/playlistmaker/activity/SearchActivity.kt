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
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.TrackAdapter
import com.practicum.playlistmaker.model.SearchResponse
import com.practicum.playlistmaker.network.AppleApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    var searchFieldContent: String? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setContentView(R.layout.activity_search)

        val trackAdapter = TrackAdapter(emptyList())
        val recycler: RecyclerView = findViewById(R.id.rv_track_list)
        recycler.adapter = trackAdapter

        fun clearTrackList() {
            trackAdapter.setTracks(emptyList())
        }

        val backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener { finish() }

        val placeholderIcon = findViewById<ImageView>(R.id.placeholderIcon)
        val placeholderText = findViewById<TextView>(R.id.placeholderText)
        val placeholderButton = findViewById<Button>(R.id.placeholderButton)

        fun showPlaceholder(text: String) {
            clearTrackList()

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
                    placeholderText.setText(R.string.placeholder_empty_error)
                }

                resources.getString(PLACEHOLDER_INTERNET_ERROR) -> {
                    placeholderIcon.setImageDrawable(getDrawable(R.attr.placeholderInternetError))
                    placeholderText.setText(R.string.placeholder_internet_error)
                    placeholderButton.visibility = View.VISIBLE
                }
            }
        }

        val searchField = findViewById<EditText>(R.id.et_search_field)
        val appleApiProvider = AppleApiProvider()

        fun query() {
            if (searchField.text.isNotEmpty()) {
                appleApiProvider.api.search(searchField.text.toString())
                    .enqueue(object : Callback<SearchResponse> {
                        override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>)
                        {
                            when (response.code()) {
                                200 -> {
                                    Log.d("RESPONSE_CODE", response.code().toString())
                                    Log.d("RESPONSE_BODY", response.body()?.results.toString())

                                    placeholderIcon.setImageDrawable(null)
                                    placeholderText.text = null
                                    placeholderButton.visibility = View.GONE

                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        trackAdapter.setTracks(response.body()?.results!!)
                                    } else {
                                        showPlaceholder(resources.getString(R.string.placeholder_empty_error))
                                    }
                                }

                                else -> {
                                    Log.d("RESPONSE_CODE", response.code().toString())
                                    showPlaceholder(resources.getString(R.string.placeholder_internet_error))
                                }
                            }
                        }

                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            Log.d("RESPONSE_ERROR", t.message.toString())
                        }
                    })
            }else{
                clearTrackList()
            }
        }

        // todo: чтобы обработать нажатие на кнопку "done", к экземпляру Edittext добаляется слушатель
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                query()
                true
            }
            false
        }

        val resetButton = findViewById<ImageButton>(R.id.button_reset)
        resetButton.setOnClickListener {
            searchField.setText("")
            clearTrackList()

            // todo: спрятать виртуальную клавиатуру
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
            }
        }
        searchField.addTextChangedListener(textWatcher)
    } // onCreate() ends...

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