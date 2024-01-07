package com.practicum.playlistmaker.data.mapper

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.model.Track

class PlayerMapper {
    fun mapTrackToData(track: Track) : TrackDto {
        with(track) {
            return TrackDto(
                trackId = trackId,
                trackName = trackName,
                artistName = artistName,
                trackTimeMillis = trackTimeMillis,
                artworkUrl100 = artworkUrl100,
                collectionName = collectionName,
                releaseDate = releaseDate,
                primaryGenreName = primaryGenreName,
                country = country,
                previewUrl = previewUrl
            )
        }
    }
}