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
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.trackList.TrackListAdapter
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.model.SearchResponse
import com.practicum.playlistmaker.network.AppleApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    var searchFieldContent: String? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        val trackListAdapter = TrackListAdapter(emptyList())
        binding.trackList.adapter = trackListAdapter

        fun clearTrackList() {
            trackListAdapter.setTracks(emptyList())
        }

        fun showRecycler(){
            binding.trackList.visibility = View.VISIBLE
        }

        fun hideRecycler(){
            binding.trackList.visibility = View.GONE
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
                    binding.placeholderIcon.setImageDrawable(getDrawable(R.attr.placeholderEmptyError))
                    binding.placeholderText.setText(PLACEHOLDER_EMPTY_ERROR)
                }

                resources.getString(PLACEHOLDER_INTERNET_ERROR) -> {
                    binding.placeholderIcon.setImageDrawable(getDrawable(R.attr.placeholderInternetError))
                    binding.placeholderText.setText(PLACEHOLDER_INTERNET_ERROR)
                    binding.placeholderButton.visibility = View.VISIBLE
                }
            }
        }

        fun hidePlaceholder(){
            binding.placeholderIcon.setImageDrawable(null)
            binding.placeholderText.text = null
            binding.placeholderButton.visibility = View.GONE
        }

        fun hideContainer()
        {
            hideRecycler()
            hidePlaceholder()
        }

        val appleApiProvider = AppleApiProvider()

        fun query()
        {
            if (binding.searchField.text.isNotEmpty()) {
                appleApiProvider.api.search(binding.searchField.text.toString())
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
                            Log.d("RESPONSE_ERROR", t.message.toString())
                            hideContainer()
                            showPlaceholder(resources.getString(PLACEHOLDER_INTERNET_ERROR))
                        }
                    })
            }else{ clearTrackList() }
        }

        // todo: чтобы обработать нажатие на кнопку "done", к экземпляру EditText добаляется слушатель:
        binding.searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                query()
                true
            }
            false
        }

        binding.placeholderButton.setOnClickListener { query() }

        binding.resetButton.setOnClickListener {
            binding.searchField.setText("")
            clearTrackList()
            hideContainer()

            // todo: спрятать виртуальную клавиатуру:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                binding.resetButton.visibility = resetButtonVisibility(string)
                if (string != null) searchFieldContent = string.toString()
            }
        }
        binding.searchField.addTextChangedListener(textWatcher)
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