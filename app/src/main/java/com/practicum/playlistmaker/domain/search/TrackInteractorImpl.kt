package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrackInteractorImpl @Inject constructor(
    private val repository: TrackRepository
): TrackInteractor {
    override fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTrack(expression).map { result ->
            when (result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }
}
