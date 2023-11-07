package ru.namerpro.playlistmaker.player.ui.fragment.state

import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel

sealed interface AddToPlaylistState{

    data class WithPlaylists(
        val playlists: List<PlaylistModel>
    ) : AddToPlaylistState

}