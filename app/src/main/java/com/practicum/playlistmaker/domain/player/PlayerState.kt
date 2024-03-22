package com.practicum.playlistmaker.domain.player

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val progress: String
)