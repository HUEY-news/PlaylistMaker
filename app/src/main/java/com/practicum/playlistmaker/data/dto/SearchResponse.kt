package com.practicum.playlistmaker.data.dto

data class SearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
): Response()

