package ru.namerpro.playlistmaker.player.ui.activity

sealed class PlayerUpdateState(
    val isPlayButtonEnabled: Boolean,
    val progress: String
) {

    class Default : PlayerUpdateState(
        isPlayButtonEnabled = false,
        progress = "00:00"
    )

    class Prepared : PlayerUpdateState(
        isPlayButtonEnabled = true,
        progress = "00:00"
    )

    class Playing(
        progress: String
    ) : PlayerUpdateState(
        isPlayButtonEnabled = true,
        progress = progress
    )

    class Paused(
        progress: String
    ) : PlayerUpdateState(
        isPlayButtonEnabled = true,
        progress = progress
    )

}