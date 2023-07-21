package ru.namerpro.playlistmaker.data.mediaplayer

import android.media.MediaPlayer
import ru.namerpro.playlistmaker.presentation.presenters.MediaPlayerInteractor
import ru.namerpro.playlistmaker.presentation.presenters.MediaPlayerListener

class MediaPlayerInteractorImpl(private val listener: MediaPlayerListener) : MediaPlayerInteractor {

    companion object {
        private const val STATUS_DEFAULT = 0
        private const val STATUS_PREPARED = 1
        private const val STATUS_PLAYING = 2
        private const val STATUS_PAUSED = 3

        const val DELAY = 100L
    }

    private var playerState = STATUS_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun getUpdateDelay() : Long {
        return DELAY
    }

    override fun playBackControl() {
        when (playerState) {
            STATUS_PLAYING -> {
                pausePlayer()
            }
            STATUS_PREPARED, STATUS_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATUS_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            listener.onPlayerCompletion()
            playerState = STATUS_PREPARED
            mediaPlayer.reset()
            preparePlayer(previewUrl)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        listener.onPlayerStart()
        playerState = STATUS_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        listener.onPlayerPause()
        playerState = STATUS_PAUSED
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }

    override fun getRunnable(): Runnable {
        return Runnable {
            if (playerState == STATUS_PLAYING) {
                listener.onPlayerTimerTick()
            }
        }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}