package com.practicum.playlistmaker.test

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestPlayerActivity: AppCompatActivity() {

    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val playerViewModel by viewModel<TestPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPlay.setOnClickListener { playerViewModel.onPlayButtonClicked() }

        playerViewModel.observePlayerState().observe(this) { playerState ->
            binding.buttonPlay.isEnabled = playerState.isPlayButtonEnabled
            binding.textViewTrackTimer.text = playerState.progress
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.onPause()
    }
}