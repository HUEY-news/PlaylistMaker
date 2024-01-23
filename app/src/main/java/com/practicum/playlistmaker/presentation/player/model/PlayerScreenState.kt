package com.practicum.playlistmaker.presentation.player.model

sealed interface PlayerScreenState {
    object Default: PlayerScreenState
    object Prepared: PlayerScreenState
    object Paused: PlayerScreenState
    data class Playing(val time: String): PlayerScreenState
}