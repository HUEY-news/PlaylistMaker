package com.practicum.playlistmaker.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.TrackAdapter
import com.practicum.playlistmaker.model.SearchResponse
import com.practicum.playlistmaker.model.Track
import com.practicum.playlistmaker.network.AppleApiProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity()
{
    var searchFieldContent : String? = null

    override fun onCreate(state: Bundle?)
    {
        super.onCreate(state)
        setContentView(R.layout.activity_search)

        val trackAdapter = TrackAdapter(emptyList())
        val recycler: RecyclerView = findViewById(R.id.rv_track_list)
        recycler.adapter = trackAdapter

        val backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener { finish() }

        // ЧТОБЫ ОБРАБОТАТЬ НАЖАТИЕ НА КНОПКУ "DONE",
        // К ЭКЗЕМПЛЯРУ EDIT_TEXT ДОБАВЛЯЕТСЯ СЛУШАТЕЛЬ:
        val searchField = findViewById<EditText>(R.id.et_search_field)
        searchField.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val appleApiProvider = AppleApiProvider()
                appleApiProvider.api
                    .search(searchField.text.toString())
                    .enqueue(object: Callback<SearchResponse> {
                        override fun onResponse(
                            call: Call<SearchResponse>,
                            response: Response<SearchResponse>) {
                            when (response.code()) {
                                200 -> {
                                    Log.d("RESPONSE_CODE", response.code().toString())
                                    Log.d("RESPONSE_BODY", response.body()?.results.toString())
                                    trackAdapter.setTracks(response.body()?.results!!)
                                }
                                else -> Log.d("RESPONSE_CODE", response.code().toString())
                            }
                        }
                        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                            Log.d("RESPONSE_ERROR", t.message.toString())
                        }
                    })
                true
            }
            false
        }

        val resetButton = findViewById<ImageButton>(R.id.button_reset)
        resetButton.setOnClickListener {
            searchField.setText("")

            trackAdapter.setTracks(emptyList())

            // СПРЯТАТЬ ВИРТУАЛЬНУЮ КЛАВИАТУРУ
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)}

        val textWatcher = object : TextWatcher
        {
            override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(string: Editable?) {}
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int)
            {
                resetButton.visibility = resetButtonVisibility(string)
                if (string != null) searchFieldContent = string.toString()
            }
        }
        searchField.addTextChangedListener(textWatcher)
    }

    fun resetButtonVisibility (string : CharSequence?) : Int
    {
        return if (string.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun onRestoreInstanceState(state: Bundle)
    {
        super.onRestoreInstanceState(state)
        if (state != null) searchFieldContent = state?.getString(SEARCH_FIELD_CONTENT, "")
    }

    override fun onSaveInstanceState(state: Bundle)
    {
        super.onSaveInstanceState(state)
        state.putString(SEARCH_FIELD_CONTENT, searchFieldContent)
    }

    companion object
    {
        const val SEARCH_FIELD_CONTENT = "SEARCH_FIELD_CONTENT"
    }
}