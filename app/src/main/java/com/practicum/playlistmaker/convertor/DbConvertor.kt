package com.practicum.playlistmaker.convertor

import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.data.db.TrackEntity
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.search.Track

class DbConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            addingTime = System.currentTimeMillis()
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            playlistCoverUri = playlist.playlistCoverUri,
            tracksIdentifiers = playlist.tracksIdentifiers,
            numberOfTracks = playlist.numberOfTracks
        )
    }

    fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlistEntity.playlistId,
            playlistName = playlistEntity.playlistName,
            playlistDescription = playlistEntity.playlistDescription,
            playlistCoverUri = playlistEntity.playlistCoverUri,
            tracksIdentifiers = playlistEntity.tracksIdentifiers,
            numberOfTracks = playlistEntity.numberOfTracks
        )
    }
}