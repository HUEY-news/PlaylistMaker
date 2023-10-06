package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchActivity : AppCompatActivity()
{
    var searchFieldContent : String? = null

    private val trackList = listOf(
        Track(
            trackName = "Smells Like Teen Spirit",
            artistName = "Nirvana",
            trackTime = "5:01",
            artWorkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ), Track(
            trackName = "Billie Jean",
            artistName = "Michael Jackson",
            trackTime = "4:35",
            artWorkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ), Track(
            trackName = "Stayin' Alive",
            artistName = "Bee Gees",
            trackTime = "4:10",
            artWorkUrl100 = "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ), Track(
            trackName = "Whole Lotta Love",
            artistName = "Led Zeppelin",
            trackTime = "5:33",
            artWorkUrl100 = "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ), Track(
            trackName = "Sweet Child O'Mine",
            artistName = "Guns N' Roses",
            trackTime = "5:03",
            artWorkUrl100 = "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )
    )
    override fun onCreate(state: Bundle?)
    {
        super.onCreate(state)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageButton>(R.id.button_back)
        backButton.setOnClickListener { finish() }

        val searchField = findViewById<EditText>(R.id.et_search_field)
        val resetButton = findViewById<ImageButton>(R.id.button_reset)
        resetButton.setOnClickListener {
            searchField.setText("")

            // СПРЯТАТЬ ВИРТУАЛЬНУЮ КЛАВИАТУРУ
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)}
            // -------------------------------

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

        val recycler: RecyclerView = findViewById(R.id.rv_track_list)
        val trackAdapter = TrackAdapter(trackList)
        recycler.adapter = trackAdapter
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