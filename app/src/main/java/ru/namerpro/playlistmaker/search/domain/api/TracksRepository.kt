package ru.namerpro.playlistmaker.search.domain.api

import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface TracksRepository {

    fun searchTracks(
        trackName: String
    ): Pair<List<TrackModel>, Int>

}