package com.practicum.playlistmaker.model

class searchResponse(val resultCount: Int, val results: ArrayList<Result>) {
    data class Result(
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: Int,
        val artworkUrl100: String
    )

}