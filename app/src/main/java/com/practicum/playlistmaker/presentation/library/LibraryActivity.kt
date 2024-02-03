package com.practicum.playlistmaker.presentation.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {

    private var _binding: ActivityLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediator: TabLayoutMediator

    private var emptyPlaceholder: Int = 0
    private lateinit var emptyFavouriteMessage: String
    private lateinit var emptyPlaylistMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emptyPlaceholder = R.drawable.empty_placeholder
        emptyFavouriteMessage = resources.getString(R.string.empty_favourite_message)
        emptyPlaylistMessage = resources.getString(R.string.empty_playlist_message)

        binding.backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.viewPager.adapter = LibraryPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
            placeholder = emptyPlaceholder,
            emptyFavouriteMessage = emptyFavouriteMessage,
            emptyPlaylistMessage = emptyPlaylistMessage
        )

        mediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0) tab.text = resources.getString(R.string.favourite_tab)
            else tab.text = resources.getString(R.string.playlist_tab)
        }
        mediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediator.detach()
    }
}