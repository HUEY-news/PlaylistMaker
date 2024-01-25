package com.practicum.playlistmaker.domain.track.api

import com.practicum.playlistmaker.domain.track.model.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrackList: List<Track>?, errorMessage: String?)
    }
}