package com.practicum.playlistmaker.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
): Parcelable {

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

