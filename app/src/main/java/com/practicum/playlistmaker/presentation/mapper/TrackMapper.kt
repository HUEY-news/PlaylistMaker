package com.practicum.playlistmaker.presentation.mapper

import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.model.TrackInfo
import com.practicum.playlistmaker.utils.convertDate
import com.practicum.playlistmaker.utils.convertTime

object TrackMapper {
    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackName = track.trackName,
            artistName = track.artistName,
            trackDuration = convertTime(track.trackTimeMillis),
            artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            collectionName = track.collectionName,
            releaseYear = convertDate(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country
        )
    }
}