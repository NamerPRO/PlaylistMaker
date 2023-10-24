package ru.namerpro.playlistmaker.player.domain.api

interface MediaPlayerRepository {

    fun playBackControl()

    fun preparePlayer(
        previewUrl: String
    )

    fun startPlayer(
        callCallback: Boolean = true
    )

    fun pausePlayer()

    fun destroyPlayer()

    fun isPlaying(): Boolean

    fun getUpdateDelay(): Long

    fun getCurrentPosition(): Int

    fun isPrepared(): Boolean

    fun setListener(
        listener: MediaPlayerListener
    )

}