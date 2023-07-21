package ru.namerpro.playlistmaker.presentation.presenters

interface MediaPlayerListener {

    fun onPlayerCompletion()
    fun onPlayerStart()
    fun onPlayerPause()
    fun onPlayerTimerTick()

}