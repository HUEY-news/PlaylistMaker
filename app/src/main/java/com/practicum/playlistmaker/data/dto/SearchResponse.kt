package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.data.track.TrackDto

data class SearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
): Response()

