package ru.namerpro.playlistmaker.domain.impl

import ru.namerpro.playlistmaker.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(trackName: String, handler: TracksInteractor.TracksCallback) {
        executor.execute {
            val response = repository.searchTracks(trackName)
            handler.handle(response.first, response.second)
        }
    }

}