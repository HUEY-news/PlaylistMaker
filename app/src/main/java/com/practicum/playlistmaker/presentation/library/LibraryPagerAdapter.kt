package com.practicum.playlistmaker.presentation.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class LibraryPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val placeholder: Int,
    private val emptyFavouriteMessage: String,
    private val emptyPlaylistMessage: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        if (position == 0) LibraryFavouriteFragment.newInstance(placeholder, emptyFavouriteMessage)
        else LibraryPlaylistFragment.newInstance(placeholder, emptyPlaylistMessage)
}