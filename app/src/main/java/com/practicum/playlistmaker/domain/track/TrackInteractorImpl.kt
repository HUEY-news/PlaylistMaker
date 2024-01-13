package com.practicum.playlistmaker.domain.track

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