package com.practicum.playlistmaker.data.network.dto

import com.practicum.playlistmaker.domain.models.Track

data class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<Track>)

