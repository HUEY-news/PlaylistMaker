package com.practicum.playlistmaker.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryBinding

class LibraryFragment: Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediator: TabLayoutMediator

    private var emptyPlaceholder: Int = 0
    private lateinit var emptyFavouriteMessage: String
    private lateinit var emptyPlaylistMessage: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyPlaceholder = R.drawable.empty_error_placeholder
        emptyFavouriteMessage = resources.getString(R.string.empty_favourite_message)
        emptyPlaylistMessage = resources.getString(R.string.empty_playlist_message)


        binding.viewPager.adapter = LibraryPagerAdapter(
            fragmentManager = childFragmentManager,
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

    override fun onDestroyView() {
        super.onDestroyView()
        mediator.detach()
    }
}