package com.practicum.playlistmaker.presentation.model

data class TrackInfo (
    val trackName: String,
    val artistName: String,
    val trackDuration: String,
    val artworkUrl512: String,
    val collectionName: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String
)
