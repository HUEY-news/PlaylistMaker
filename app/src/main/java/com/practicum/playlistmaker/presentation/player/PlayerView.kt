package com.practicum.playlistmaker.presentation.player

interface PlayerView {

    fun setPauseImage()
    fun setPlayImage()

    fun setTimer(time: String)
    fun resetTimer()
}