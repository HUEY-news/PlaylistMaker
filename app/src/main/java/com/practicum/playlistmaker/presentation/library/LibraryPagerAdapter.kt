package com.practicum.playlistmaker.presentation.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class LibraryPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val placeholder: Int,
    private val emptyPlaylistMessage: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> LibraryFavoriteFragment()
            else -> LibraryPlaylistFragment.newInstance(placeholder, emptyPlaylistMessage)
        }
}