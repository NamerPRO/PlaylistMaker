package ru.namerpro.playlistmaker.player.data.media_player

sealed interface MediaPlayerState {

    object Default : MediaPlayerState

    object Prepared : MediaPlayerState

    object Playing : MediaPlayerState

    object Paused : MediaPlayerState

}