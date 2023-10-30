package ru.namerpro.playlistmaker.common.domain.api

import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface  TracksInPlaylistDatabaseInteractor {

    suspend fun addToTrackInPlaylistStorage(
        track: TrackModel,
        trackAddTime: Long
    )

    suspend fun isInTrackInPlaylistStorage(
        trackId: Long
    ): Boolean

}