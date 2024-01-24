package com.practicum.playlistmaker.domain.track.impl

import com.practicum.playlistmaker.domain.track.api.TrackInteractor
import com.practicum.playlistmaker.domain.track.repository.TrackRepository
import com.practicum.playlistmaker.util.Resource

class TrackInteractorImpl(
    private val repository: TrackRepository
): TrackInteractor {
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        Thread {
            when (val resource = repository.searchTrack(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }.start()
    }
}