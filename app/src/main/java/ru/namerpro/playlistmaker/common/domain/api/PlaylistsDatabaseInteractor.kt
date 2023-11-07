package ru.namerpro.playlistmaker.common.domain.api

import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface PlaylistsDatabaseInteractor {

    suspend fun addPlaylist(
        playlist: PlaylistModel
    )

    suspend fun deletePlaylist(
        playlist: PlaylistModel
    )

    suspend fun getPlaylist(): List<PlaylistModel>

    suspend fun getPlaylistCount(): Int

    suspend fun hasPlaylist(
        playlistTitle: String
    ): Boolean

    suspend fun addTrackToPlaylist(
        playlistTitle: String,
        tracksInPlaylistIds: ArrayList<Long>
    )

}