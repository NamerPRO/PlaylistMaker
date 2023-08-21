package ru.namerpro.playlistmaker.search.domain.impl

import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(
        trackName: String,
        handler: TracksInteractor.TracksCallback
    ) {
        executor.execute {
            val response = repository.searchTracks(trackName)
            handler.handle(response.first, response.second)
        }
    }

}