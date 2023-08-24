package ru.namerpro.playlistmaker.player.ui.activity

sealed interface PlayerUpdateState {

    object Completion : PlayerUpdateState

    object Start : PlayerUpdateState

    object Pause : PlayerUpdateState

    object Tick : PlayerUpdateState

    object Prepared: PlayerUpdateState

}