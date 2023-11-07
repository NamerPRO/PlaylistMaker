package ru.namerpro.playlistmaker.common.domain.api

import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface TracksInPlaylistDatabaseRepository {

    suspend fun addToTrackInPlaylistStorage(
        track: TrackModel,
        trackAddTime: Long
    )

    suspend fun isInTrackInPlaylistStorage(
        trackId: Long
    ): Boolean

    suspend fun getTrackFromStorage(
        trackId: Long
    ): TrackModel

    suspend fun deleteTrackFromStorage(
        track: TrackModel
    )

}