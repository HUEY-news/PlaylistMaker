package com.practicum.playlistmaker.network

import com.practicum.playlistmaker.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleApi {

    @GET("/search?entity=song")
    fun search(
        @Query("term") text: String
    ): Call<SearchResponse>

}