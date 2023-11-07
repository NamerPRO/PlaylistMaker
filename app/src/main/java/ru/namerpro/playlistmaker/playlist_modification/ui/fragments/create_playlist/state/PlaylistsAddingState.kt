package ru.namerpro.playlistmaker.playlist_modification.ui.fragments.create_playlist.state

import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel

sealed interface PlaylistsAddingState {

    data class PlaylistAddSuccess(
        val playlist: PlaylistModel
    ) : PlaylistsAddingState

    object PlaylistAddError : PlaylistsAddingState

}