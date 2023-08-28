package ru.namerpro.playlistmaker.search.domain.impl

import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository
import java.util.concurrent.ExecutorService

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val executor: ExecutorService
) : TracksInteractor {

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