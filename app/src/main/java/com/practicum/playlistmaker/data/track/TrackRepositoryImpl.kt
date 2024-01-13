package com.practicum.playlistmaker.data.track

import com.practicum.playlistmaker.data.dto.SearchRequest
import com.practicum.playlistmaker.data.dto.SearchResponse
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.track.Track
import com.practicum.playlistmaker.domain.track.TrackRepository
import com.practicum.playlistmaker.util.Resource

class TrackRepositoryImpl(
    private val networkClient: NetworkClient
) : TrackRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(SearchRequest(expression))

        return when (response.resultCode) {
            -1 -> Resource.Error("Проверьте подключение к интернету")

            200 -> {
                val result = (response as SearchResponse).results
                if (result.isNotEmpty()) {
                    Resource.Success(response.results.map {
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
                    })
                } else Resource.Error("Ничего не нашлось")
            }

            else -> Resource.Error("Ошибка сервера")
        }
    }
}
