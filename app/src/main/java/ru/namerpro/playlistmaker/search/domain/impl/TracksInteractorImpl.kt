package ru.namerpro.playlistmaker.search.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    override fun searchTracks(
        trackName: String
    ): Flow<Pair<List<TrackModel>, Int>> {
        return repository.searchTracks(trackName)
    }

}