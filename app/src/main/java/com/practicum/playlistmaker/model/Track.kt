package com.practicum.playlistmaker.model

import android.os.Parcel
import android.os.Parcelable
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

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeInt(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel)
            = Track(
                parcel.readInt(),
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readInt(),
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!
            )

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}
