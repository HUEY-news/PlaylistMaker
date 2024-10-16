package com.practicum.playlistmaker.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getThemeStateLiveData().observe(viewLifecycleOwner) { state ->
            binding.themeSwitcher.isChecked = state
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeState(checked)
        }

        binding.shareContainer.setOnClickListener { viewModel.shareApp() }
        binding.agreementContainer.setOnClickListener { viewModel.openTerms() }
        binding.supportContainer.setOnClickListener { viewModel.openSupport() }
    }
}