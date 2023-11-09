package ru.namerpro.playlistmaker.playlist_modification.ui.fragments.edit_playlist.state

import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel

sealed interface PlaylistUpdateState {

    data class UpdatedPlaylist(
        val playlist: PlaylistModel
    ) : PlaylistUpdateState

    object PlaylistUpdateError : PlaylistUpdateState

}