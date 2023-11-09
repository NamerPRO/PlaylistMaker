package ru.namerpro.playlistmaker.common.data

import ru.namerpro.playlistmaker.common.data.converters.PlaylistsDbConvertor
import ru.namerpro.playlistmaker.common.data.db.PlaylistsDatabase
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseRepository
import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseRepository
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class PlaylistsDatabaseRepositoryImpl(
    private val playlistsDatabase: PlaylistsDatabase,
    private val playlistsDbConvertor: PlaylistsDbConvertor,
    private val tracksInPlaylistDatabaseRepository: TracksInPlaylistDatabaseRepository
) : PlaylistsDatabaseRepository {

    override suspend fun addPlaylist(
        playlist: PlaylistModel
    ) {
        playlistsDatabase.getPlaylistDao().addPlaylist(playlistsDbConvertor.map(playlist))
    }

    override suspend fun deletePlaylist(
        playlist: PlaylistModel
    ) {
        playlistsDatabase.getPlaylistDao().deletePlaylist(playlistsDbConvertor.map(playlist))
    }

    override suspend fun getPlaylist(): List<PlaylistModel> {
        return playlistsDatabase.getPlaylistDao().getPlaylists().map { playlistEntity -> playlistsDbConvertor.map(playlistEntity) }
    }

    override suspend fun getPlaylistCount(): Int {
        return playlistsDatabase.getPlaylistDao().getPlaylistsCount()
    }

    override suspend fun hasPlaylist(
        playlistTitle: String
    ): Boolean {
        return playlistsDatabase.getPlaylistDao().hasPlaylist(playlistTitle) == PLAY_LIST_EXISTS
    }

    override suspend fun updateTracksInPlaylist(
        playlistTitle: String,
        tracksInPlaylistIds: ArrayList<Long>
    ) {
        playlistsDatabase.getPlaylistDao().updateTrackIdsInPlaylist(playlistTitle, tracksInPlaylistIds)
    }

    override suspend fun updatePlaylist(
        playlist: PlaylistModel
    ) {
        playlistsDatabase.getPlaylistDao().updatePlaylist(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.coverFull,
            playlist.coverCut,
            playlist.time
        )
    }

    override suspend fun deleteTrackIfItDoesNotExistInAnyOfPlaylists(
        track: TrackModel
    ) {
        val trackExistsInAnyOfPlaylists = playlistsDatabase.getPlaylistDao().getPlaylists().any { playlist ->
            playlist.tracksIds.contains(track.trackId)
        }
        if (!trackExistsInAnyOfPlaylists) {
            tracksInPlaylistDatabaseRepository.deleteTrackFromStorage(track)
        }
    }

    companion object {
        private const val PLAY_LIST_EXISTS = 1
    }

}