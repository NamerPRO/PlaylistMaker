package ru.namerpro.playlistmaker.common.data

import ru.namerpro.playlistmaker.common.data.converters.TrackInPlaylistDbConvertor
import ru.namerpro.playlistmaker.common.data.db.TracksInPlaylistDatabase
import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class TracksInPlaylistDatabaseRepositoryImpl(
    private val tracksInPlaylistDatabase: TracksInPlaylistDatabase,
    private val trackInPlaylistDbConvertor: TrackInPlaylistDbConvertor
) : TracksInPlaylistDatabaseRepository {

    override suspend fun addToTrackInPlaylistStorage(
        track: TrackModel,
        trackAddTime: Long
    ) {
        tracksInPlaylistDatabase.getTracksInPlaylistDao().addToTrackInPlaylistStorage(trackInPlaylistDbConvertor.map(track, trackAddTime))
    }

    override suspend fun isInTrackInPlaylistStorage(
        trackId: Long
    ): Boolean {
        return tracksInPlaylistDatabase.getTracksInPlaylistDao().isInTrackInPlaylistStorage(trackId) == HAS_TRACK
    }

    companion object {
        private const val HAS_TRACK = 1
    }

}