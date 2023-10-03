package ru.namerpro.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface TracksRepository {

    fun searchTracks(
        trackName: String
    ): Flow<Pair<List<TrackModel>, Int>>

}