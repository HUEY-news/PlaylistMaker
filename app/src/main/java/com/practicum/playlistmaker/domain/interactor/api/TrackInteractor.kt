package com.practicum.playlistmaker.domain.interactor.api

import com.practicum.playlistmaker.domain.entity.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrackList: List<Track>)
    }
}