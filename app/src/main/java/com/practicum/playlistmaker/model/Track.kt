package com.practicum.playlistmaker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

@Parcelize
data class Track(
    val trackId: Int, // идентификатор трека
    val trackName: String, // название композиции
    val artistName: String, // имя исполнителя
    val trackTimeMillis: Int, // продолжительность трека
    val artworkUrl100: String, // ссылка на изображение обложки
    val collectionName: String, // название альбома
    val releaseDate: String, // год релиза трека
    val primaryGenreName: String, // жанр трека
    val country: String, // страна исполнителя
    val previewUrl: String // ссылка на отрывок трека в формате String
): Parcelable{

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

