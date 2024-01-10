package com.practicum.playlistmaker.data.repository

import com.practicum.playlistmaker.data.dto.SearchRequest
import com.practicum.playlistmaker.data.dto.SearchResponse
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.repository.TrackRepository

class TrackRepositoryImpl(
    private val networkClient: NetworkClient
) : TrackRepository {
    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as SearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}