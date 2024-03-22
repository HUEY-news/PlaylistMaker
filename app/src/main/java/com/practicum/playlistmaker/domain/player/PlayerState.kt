package com.practicum.playlistmaker.domain.player

sealed class PlayerState(
    val isPlayButtonEnabled: Boolean,
    val progress: String
) {
    class Default: PlayerState(false, "00:00")
    class Prepared: PlayerState(true, "00:00")
    class Playing(progress: String): PlayerState(true, progress)
    class Paused(progress: String): PlayerState(true, progress)
}