package com.practicum.playlistmaker.domain.track

class TrackInteractorImpl(
    private val repository: TrackRepository
): TrackInteractor {
    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        Thread {
            consumer.consume(repository.searchTrack(expression))
        }.start()
    }
}