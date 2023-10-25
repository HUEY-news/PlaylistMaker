package com.practicum.playlistmaker.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppleApiProvider {
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(AppleApi::class.java)
}