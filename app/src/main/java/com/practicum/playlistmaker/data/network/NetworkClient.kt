package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}