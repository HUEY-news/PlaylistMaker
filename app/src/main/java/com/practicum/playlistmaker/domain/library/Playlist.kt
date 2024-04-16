package com.practicum.playlistmaker.domain.library

data class Playlist (
    val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val playlistCoverUri: String?,
    val tracksIdentifiers: String,
    val numberOfTracks: Int
)
