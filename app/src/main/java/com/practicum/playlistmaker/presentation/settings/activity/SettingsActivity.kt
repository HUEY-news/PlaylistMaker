package com.practicum.playlistmaker.presentation.settings.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity()
{
    private val viewModel by viewModel<SettingsViewModel>()
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.i("TEST", "SettingsActivity СОЗДАНА")
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeStateLiveData().observe(this) { state ->
            binding.themeSwitcher.isChecked = state
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeState(checked)
        }

        binding.backButton.setOnClickListener { finish() }
        binding.shareContainer.setOnClickListener { viewModel.shareApp() }
        binding.agreementContainer.setOnClickListener { viewModel.openTerms() }
        binding.supportContainer.setOnClickListener { viewModel.openSupport() }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TEST", "SettingsActivity УНИЧТОЖЕНА")
    }
}