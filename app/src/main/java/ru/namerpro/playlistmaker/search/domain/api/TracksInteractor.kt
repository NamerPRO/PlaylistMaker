package ru.namerpro.playlistmaker.search.domain.api

import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface TracksInteractor {

    fun searchTracks(trackName: String, handler: TracksCallback)

    interface TracksCallback {
        fun handle(foundTracks: List<TrackModel>, responseCode: Int)
    }

}