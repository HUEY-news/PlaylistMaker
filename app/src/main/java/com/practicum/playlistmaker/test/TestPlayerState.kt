package com.practicum.playlistmaker.test

sealed class TestPlayerState(
    val isPlayButtonEnabled: Boolean,
    val progress: String
) {
    class Default: TestPlayerState(false, "00:00")
    class Prepared: TestPlayerState(true, "00:00")
    class Playing(progress: String): TestPlayerState(true, progress)
    class Paused(progress: String): TestPlayerState(true, progress)
}