package com.practicum.playlistmaker.convertor

import com.practicum.playlistmaker.data.db.FavoriteTrackEntity
import com.practicum.playlistmaker.data.db.PlaylistEntity
import com.practicum.playlistmaker.data.db.SavedTrackEntity
import com.practicum.playlistmaker.domain.library.Playlist
import com.practicum.playlistmaker.domain.search.Track

class DbConvertor {

    fun mapTrackToFavorite(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
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

    fun mapFavoriteToTrack(favoriteTrackEntity: FavoriteTrackEntity): Track {
        return Track(
            trackId = favoriteTrackEntity.trackId,
            trackName = favoriteTrackEntity.trackName,
            artistName = favoriteTrackEntity.artistName,
            trackTimeMillis = favoriteTrackEntity.trackTimeMillis,
            artworkUrl100 = favoriteTrackEntity.artworkUrl100,
            collectionName = favoriteTrackEntity.collectionName,
            releaseDate = favoriteTrackEntity.releaseDate,
            primaryGenreName = favoriteTrackEntity.primaryGenreName,
            country = favoriteTrackEntity.country,
            previewUrl = favoriteTrackEntity.previewUrl
        )
    }

    fun mapTrackToSaved(track: Track): SavedTrackEntity {
        return SavedTrackEntity(
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

    fun mapSavedToTrack(trackEntity: SavedTrackEntity): Track {
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