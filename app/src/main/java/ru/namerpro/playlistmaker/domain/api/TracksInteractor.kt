package ru.namerpro.playlistmaker.domain.api

import ru.namerpro.playlistmaker.domain.models.Track

interface TracksInteractor {

    fun searchTracks(trackName: String, handler: TracksCallback)

    interface TracksCallback {
        fun handle(foundTracks: List<Track>, responseCode: Int)
    }

}