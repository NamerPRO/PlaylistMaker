package ru.namerpro.playlistmaker.media.ui.fragments.playlist.state

sealed interface PlaylistsAddingState {

    data class PlaylistAddSuccess(
        val playlistTitle: String
    ) : PlaylistsAddingState

    object PlaylistAddError : PlaylistsAddingState

}