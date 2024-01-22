package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModelFactory

class SettingsActivity : AppCompatActivity()
{
    private lateinit var viewModel: SettingsViewModel
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.i("TEST", "SettingsActivity СОЗДАНА")
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SettingsViewModelFactory()).get(SettingsViewModel::class.java)

        binding.themeSwitcher.isChecked = viewModel.getThemeState()
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