package com.practicum.playlistmaker.presentation.player

sealed interface PlayerStateSealedInterface {
    object Default: PlayerStateSealedInterface
    object Prepared: PlayerStateSealedInterface
    object Paused: PlayerStateSealedInterface
    data class Playing(val time: String): PlayerStateSealedInterface
}