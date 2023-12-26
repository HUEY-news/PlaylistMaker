package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.network.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleApi
{
    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String
    ): Call<SearchResponse>
}