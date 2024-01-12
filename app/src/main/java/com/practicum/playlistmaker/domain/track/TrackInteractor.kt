package com.practicum.playlistmaker.domain.track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrackList: List<Track>?)
    }
}