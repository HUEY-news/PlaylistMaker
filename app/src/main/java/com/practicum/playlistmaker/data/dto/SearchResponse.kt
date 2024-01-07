package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.model.Track

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>
): Response()

