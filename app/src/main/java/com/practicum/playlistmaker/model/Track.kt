package com.practicum.playlistmaker.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

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
){

    fun getCoverArtwork(): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    fun getReleaseYear(): String {
        val dateString: String = releaseDate
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = dateFormat.parse(dateString)
        val yearFormat: DateFormat = SimpleDateFormat("yyyy")
        return yearFormat.format(date)
    }
}
