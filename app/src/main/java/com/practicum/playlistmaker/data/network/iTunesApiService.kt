package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApiService
{
    @GET("/search?entity=song")
    suspend fun searchTrack(
        @Query("term") text: String
    ): SearchResponse
}