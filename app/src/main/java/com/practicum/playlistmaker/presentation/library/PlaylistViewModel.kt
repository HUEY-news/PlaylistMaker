package com.practicum.playlistmaker.presentation.library

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.library.PlaylistInteractor

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {
}