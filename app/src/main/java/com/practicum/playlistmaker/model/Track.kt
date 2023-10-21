package com.practicum.playlistmaker.model

data class Track(
    val trackName: String, // НАЗВАНИЕ КОМПОЗИЦИИ
    val artistName: String, // ИМЯ ИСПОЛНИТЕЛЯ
    val trackTimeMillis: Int, // ПРОДОЛЖИТЕЛЬНОСТЬ ТРЕКА
    val artworkUrl100: String // ССЫЛКА НА ИЗОБРАЖЕНИЕ ОБЛОЖКИ
)
