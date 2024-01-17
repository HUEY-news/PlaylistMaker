package com.practicum.playlistmaker.ui.search

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
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.presentation.search.SearchView
import com.practicum.playlistmaker.ui.player.PlayerActivity
import com.practicum.playlistmaker.util.Creator

class SearchActivity : AppCompatActivity(), SearchView {

    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!

    private var textWatcher: TextWatcher? = null
    private lateinit var searchField: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var searchRecycler: RecyclerView
    private lateinit var historyRecycler: RecyclerView

    private lateinit var errorText: String
    private lateinit var emptyErrorText: String
    private lateinit var internetErrorText: String
    private lateinit var serverErrorText: String
    private var emptyErrorPlaceholder: Int = 0
    private var internetErrorPlaceholder: Int = 0

    private val searchHistory = SearchHistory(App.sharedPreferences)
    private val searchAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            searchHistory.addTrackToHistory(track)
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.TRACK_ID, track)
            startActivity(intent)
        }
    }
    private val historyAdapter = TrackAdapter { track ->
        if (clickDebounce()) {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(PlayerActivity.TRACK_ID, track)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchPresenter = Creator.provideSearchPresenter (
        searchView = this,
        context = this
    )


    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchField = findViewById(R.id.searchField)
        progressBar = findViewById(R.id.progressBar)
        searchRecycler = findViewById(R.id.searchRecycler)
        historyRecycler = findViewById(R.id.historyRecycler)

        searchRecycler.adapter = searchAdapter
        historyRecycler.adapter = historyAdapter

        errorText = resources.getString(R.string.placeholder_error)
        emptyErrorText = resources.getString(R.string.placeholder_empty_error)
        internetErrorText = resources.getString(R.string.placeholder_internet_error)
        serverErrorText = resources.getString(R.string.placeholder_server_error)
        emptyErrorPlaceholder = R.attr.placeholderEmptyError
        internetErrorPlaceholder = R.attr.placeholderInternetError

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }

        if (searchHistory.getHistory().isNotEmpty()) {
            historyAdapter.setItems(searchHistory.getHistory())
            binding.layoutSearchHistory.searchHistoryContainer.isVisible = true
        }

        // реакция на нажатие кнопки "очистить историю":
        binding.layoutSearchHistory.searchHistoryButton.setOnClickListener {
            binding.layoutSearchHistory.searchHistoryContainer.isVisible = false
            searchHistory.clearHistory()
        }

        // реализация отслеживания состояния фокуса поля поиска:
        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            if (searchHistory.getHistory().isNotEmpty()) {
                historyAdapter.setItems(searchHistory.getHistory())
                binding.layoutSearchHistory.searchHistoryContainer.isVisible =
                    hasFocus && binding.searchField.text.isEmpty()
            }
        }

        // реакция на нажатие кнопки "обновить":
        findViewById<Button>(R.id.placeholderButton).setOnClickListener {
            searchPresenter.searchRequest(searchField.text.toString())
        }

        // реакция на нажатие кнопки сброса:
        binding.resetButton.setOnClickListener {
            binding.searchField.setText("")
            clearTrackList()
            // спрятать виртуальную клавиатуру:
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                binding.searchField.windowToken,
                0
            )
        }

        // реакция на изменение текста в поле поиска:
        textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) hidePlaceholder()
                binding.resetButton.isVisible = !s.isNullOrEmpty()
                clearTrackList()
                searchPresenter.searchDebounce(changedText = s?.toString() ?: "")

                if (searchHistory.getHistory().isNotEmpty()) {
                    historyAdapter.setItems(searchHistory.getHistory())
                    binding.layoutSearchHistory.searchHistoryContainer.isVisible =
                        binding.searchField.hasFocus() && s?.isEmpty() == true
                }
            }
        }
        textWatcher?.let { searchField.addTextChangedListener(it) }
    }


    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { searchField.removeTextChangedListener(it) }
        searchPresenter.onDestroy()
    }


    override fun showPlaceholder(errorMessage: String) {
        clearTrackList()
        hideRecycler()

        when (errorMessage) {
            emptyErrorText -> setEmptyErrorState(errorMessage)
            internetErrorText -> setInternetErrorState(errorMessage)
            serverErrorText -> setInternetErrorState(errorMessage)
        }
    }
    private fun setEmptyErrorState(errorMessage: String) {
        binding.layoutPlaceholder.placeholderIcon.setImageDrawable(getAttribute(emptyErrorPlaceholder))
        binding.layoutPlaceholder.placeholderText.text = errorMessage
        binding.layoutPlaceholder.placeholderButton.isVisible = false
    }
    private fun setInternetErrorState(errorMessage:String) {
        binding.layoutPlaceholder.placeholderIcon.setImageDrawable(getAttribute(internetErrorPlaceholder))
        val resultErrorMessage = errorText + errorMessage
        binding.layoutPlaceholder.placeholderText.text = resultErrorMessage
        binding.layoutPlaceholder.placeholderButton.isVisible = true
    }
    private fun getAttribute(attr: Int): Drawable? {
        val attrs = intArrayOf(attr)
        val typedArray = theme.obtainStyledAttributes(attrs)
        val placeholderResourceId = typedArray.getResourceId(0, 0)
        typedArray.recycle()

        return ContextCompat.getDrawable(this, placeholderResourceId)
    }

    override fun updateTrackList(newTrackList: List<Track>) {
        searchAdapter.setItems(newTrackList)
    }
    override fun clearTrackList() {
        searchAdapter.setItems(arrayListOf())
    }

    private fun hideRecycler() {
        binding.searchRecycler.visibility = View.GONE
    }

    override fun hidePlaceholder() {
        binding.layoutPlaceholder.placeholderIcon.setImageDrawable(null)
        binding.layoutPlaceholder.placeholderText.text = null
        binding.layoutPlaceholder.placeholderButton.isVisible = false
    }

    override fun showProgressBar(isVisible: Boolean) {
        progressBar.isVisible = isVisible
    }
    override fun showSearchRecycler(isVisible: Boolean) {
        searchRecycler.isVisible = isVisible
    }


    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}