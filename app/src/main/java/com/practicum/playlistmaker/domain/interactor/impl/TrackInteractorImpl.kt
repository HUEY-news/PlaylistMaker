package com.practicum.playlistmaker.domain.interactor.impl

import com.practicum.playlistmaker.domain.interactor.api.TrackInteractor
import com.practicum.playlistmaker.domain.repository.TrackRepository

class TrackInteractorImpl(
    private val repository: TrackRepository
): TrackInteractor {
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        Thread {
            consumer.consume(repository.searchTrack(expression))
        }.start()
    }
}