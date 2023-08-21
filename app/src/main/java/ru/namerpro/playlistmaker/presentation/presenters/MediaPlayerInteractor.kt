package ru.namerpro.playlistmaker.presentation.presenters

interface MediaPlayerInteractor {

    fun playBackControl()
    fun preparePlayer(previewUrl: String)
    fun startPlayer()
    fun pausePlayer()
    fun destroyPlayer()
    fun getRunnable() : Runnable
    fun getUpdateDelay() : Long
    fun getCurrentPosition() : Int

}