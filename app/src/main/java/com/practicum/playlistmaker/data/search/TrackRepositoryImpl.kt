package com.practicum.playlistmaker.data.search

import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.db.AppDatabase
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
    private val client: NetworkClient,
    private val appDatabase: AppDatabase
) : TrackRepository {

    private val errorEmptyText: String = context.resources.getString(R.string.error_empty_text)
    private val errorInternetText: String = context.resources.getString(R.string.error_internet_text)
    private val errorServerText: String = context.resources.getString(R.string.error_server_text)

    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = client.doRequest(SearchRequest(expression))
        when (response.resultCode) {

            -1 -> emit(Resource.Error(errorInternetText))

            200 -> {
                val trackDtoList = (response as SearchResponse).results
                val trackList = trackDtoList.map {
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

                val idList = appDatabase.trackDao().getFavoriteIdList()
                for (track in trackList) track.isFavorite = idList.contains(track.trackId)

                if (trackList.isNotEmpty()) emit(Resource.Success(trackList))
                else emit(Resource.Error(errorEmptyText))
            }
            else -> emit(Resource.Error(errorServerText))
        }
    }
}