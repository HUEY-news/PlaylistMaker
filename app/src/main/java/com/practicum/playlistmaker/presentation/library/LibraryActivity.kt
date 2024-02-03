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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.viewPager.adapter = LibraryPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle
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