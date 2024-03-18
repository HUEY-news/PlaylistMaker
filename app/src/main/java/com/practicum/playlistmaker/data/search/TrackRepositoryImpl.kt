package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.dto.SearchRequest
import com.practicum.playlistmaker.data.dto.SearchResponse
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val client: NetworkClient
) : TrackRepository {

    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = client.doRequest(SearchRequest(expression))
        when (response.resultCode) {

            -1 -> emit(Resource.Error("Проверьте подключение к интернету"))

            200 -> {
                val data = (response as SearchResponse).results.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                }
                if (data.isNotEmpty()) emit(Resource.Success(data))
                else emit(Resource.Error("Ничего не нашлось"))
            }
            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }
}
