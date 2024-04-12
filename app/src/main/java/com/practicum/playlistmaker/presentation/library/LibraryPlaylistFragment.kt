package com.practicum.playlistmaker.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding

class LibraryPlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var placeholder: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeholder = requireArguments().getInt(PLACEHOLDER)
        binding.placeholderIcon.setImageResource(placeholder)
        binding.placeholderText.text = requireArguments().getString(MESSAGE)

        binding.placeholderButton.setOnClickListener {
            requireParentFragment().findNavController().navigate(R.id.action_create_new_playlist)
        }
    }

    companion object {
        private const val PLACEHOLDER = "placeholder"
        private const val MESSAGE = "message"

        fun newInstance(placeholder: Int, message: String) =
            LibraryPlaylistFragment().apply {
            arguments = bundleOf(
                PLACEHOLDER to placeholder,
                MESSAGE to message)
        }
    }
}