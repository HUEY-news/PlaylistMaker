package com.practicum.playlistmaker.model

data class Track(
    val trackName: String, // НАЗВАНИЕ КОМПОЗИЦИИ
    val artistName: String, // ИМЯ ИСПОЛНИТЕЛЯ
    val trackTime: String, // ПРОДОЛЖИТЕЛЬНОСТЬ ТРЕКА
    val artWorkUrl100: String // ССЫЛКА НА ИЗОБРАЖЕНИЕ ОБЛОЖКИ
)