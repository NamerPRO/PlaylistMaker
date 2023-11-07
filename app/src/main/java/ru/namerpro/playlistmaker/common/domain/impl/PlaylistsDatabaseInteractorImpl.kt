package ru.namerpro.playlistmaker.common.domain.impl

import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseRepository
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class PlaylistsDatabaseInteractorImpl(
    private val playlistsDatabaseRepository: PlaylistsDatabaseRepository
) : PlaylistsDatabaseInteractor {

    override suspend fun addPlaylist(
        playlist: PlaylistModel
    ) {
        playlistsDatabaseRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(
        playlist: PlaylistModel
    ) {
        playlistsDatabaseRepository.deletePlaylist(playlist)
    }

    override suspend fun getPlaylist(): List<PlaylistModel> {
        return playlistsDatabaseRepository.getPlaylist()
    }

    override suspend fun getPlaylistCount(): Int {
        return playlistsDatabaseRepository.getPlaylistCount()
    }

    override suspend fun hasPlaylist(
        playlistTitle: String
    ): Boolean {
        return playlistsDatabaseRepository.hasPlaylist(playlistTitle)
    }

    override suspend fun addTrackToPlaylist(
        playlistTitle: String,
        tracksInPlaylistIds: ArrayList<Long>
    ) {
        playlistsDatabaseRepository.addTrackIdToPlaylist(playlistTitle, tracksInPlaylistIds)
    }

}