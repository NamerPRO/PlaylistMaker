package ru.namerpro.playlistmaker.player.domain.impl

import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerListener
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {

    override fun playBackControl() {
        mediaPlayerRepository.playBackControl()
    }

    override fun preparePlayer(
        previewUrl: String
    ) {
        mediaPlayerRepository.preparePlayer(previewUrl)
    }

    override fun startPlayer(
        callCallback: Boolean
    ) {
        mediaPlayerRepository.startPlayer(callCallback)
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun destroyPlayer() {
        mediaPlayerRepository.destroyPlayer()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayerRepository.isPlaying()
    }

    override fun getUpdateDelay(): Long {
        return mediaPlayerRepository.getUpdateDelay()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun isPrepared(): Boolean {
        return mediaPlayerRepository.isPrepared()
    }

    override fun setListener(
        listener: MediaPlayerListener
    ) {
        mediaPlayerRepository.setListener(listener)
    }

}