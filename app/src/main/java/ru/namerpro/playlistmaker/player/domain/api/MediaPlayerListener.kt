package ru.namerpro.playlistmaker.player.domain.api

interface MediaPlayerListener {

    fun onPlayerCompletion()

    fun onPlayerStart()

    fun onPlayerPause()

    fun onPlayerTimerTick()

    fun onPlayerPrepared()

}