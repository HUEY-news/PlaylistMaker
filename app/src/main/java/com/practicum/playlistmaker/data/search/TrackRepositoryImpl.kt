package com.practicum.playlistmaker.data.search

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.dto.SearchRequest
import com.practicum.playlistmaker.data.dto.SearchResponse
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.search.Track
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val context: Context,
    private val client: NetworkClient
) : TrackRepository {

    private val errorEmptyText: String = context.resources.getString(R.string.error_empty_text)
    private val errorInternetText: String = context.resources.getString(R.string.error_internet_text)
    private val errorServerText: String = context.resources.getString(R.string.error_server_text)

    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = client.doRequest(SearchRequest(expression))
        when (response.resultCode) {

            -1 -> emit(Resource.Error(errorInternetText))

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
                else emit(Resource.Error(errorEmptyText))
            }
            else -> emit(Resource.Error(errorServerText))
        }
    }
}