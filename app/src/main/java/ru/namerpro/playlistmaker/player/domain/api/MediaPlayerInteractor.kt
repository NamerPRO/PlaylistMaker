package ru.namerpro.playlistmaker.player.domain.api

interface MediaPlayerInteractor {

    fun playBackControl()

    fun preparePlayer(
        previewUrl: String
    )

    fun startPlayer()

    fun pausePlayer()

    fun destroyPlayer()

    fun getRunnable(): Runnable

    fun getUpdateDelay(): Long

    fun getCurrentPosition(): Int

    fun isPrepared(): Boolean

    fun setListener(
        listener: MediaPlayerListener
    )

}