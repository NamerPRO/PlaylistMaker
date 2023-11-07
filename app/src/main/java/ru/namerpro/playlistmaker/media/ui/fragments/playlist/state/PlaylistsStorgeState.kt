package ru.namerpro.playlistmaker.media.ui.fragments.playlist.state

import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel

sealed interface PlaylistsStorgeState {

    object Empty : PlaylistsStorgeState

    data class WithPlaylists(
        val playlists: List<PlaylistModel>
    ) : PlaylistsStorgeState

}