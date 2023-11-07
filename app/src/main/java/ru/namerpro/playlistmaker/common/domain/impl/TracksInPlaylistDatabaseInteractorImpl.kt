package ru.namerpro.playlistmaker.common.domain.impl

import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class TracksInPlaylistDatabaseInteractorImpl(
    private val tracksInPlaylistDatabaseRepository: TracksInPlaylistDatabaseRepository
) : TracksInPlaylistDatabaseInteractor {

    override suspend fun addToTrackInPlaylistStorage(
        track: TrackModel,
        trackAddTime: Long
    ) {
        tracksInPlaylistDatabaseRepository.addToTrackInPlaylistStorage(track, trackAddTime)
    }

    override suspend fun isInTrackInPlaylistStorage(
        trackId: Long
    ): Boolean {
        return tracksInPlaylistDatabaseRepository.isInTrackInPlaylistStorage(trackId)
    }

}