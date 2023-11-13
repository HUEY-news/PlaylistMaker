package com.practicum.playlistmaker.model

data class Track(
    val trackId: Int, // ИДЕНТИФИКАТОР ТРЕКА
    val trackName: String, // НАЗВАНИЕ КОМПОЗИЦИИ
    val artistName: String, // ИМЯ ИСПОЛНИТЕЛЯ
    val trackTimeMillis: Int, // ПРОДОЛЖИТЕЛЬНОСТЬ ТРЕКА
    val artworkUrl100: String, // ССЫЛКА НА ИЗОБРАЖЕНИЕ ОБЛОЖКИ
    val collectionName: String, // НАЗВАНИЕ АЛЬБОМА
    val releaseDate: String, // ГОД РЕЛИЗА ТРЕКА
    val primaryGenreName: String, // ЖАНР ТРЕКА
    val country: String // СТРАНА ИСПОЛНИТЕЛЯ
)
