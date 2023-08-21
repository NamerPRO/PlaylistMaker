package ru.namerpro.playlistmaker.player.data.media_player

import android.media.MediaPlayer
import android.widget.Toast
import ru.namerpro.playlistmaker.PlaylistApplication
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerListener
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(
    private val listener: MediaPlayerListener
) : MediaPlayerRepository {

    companion object {
        const val DELAY = 100L
    }

    private var playerState: MediaPlayerState = MediaPlayerState.Default
    private val mediaPlayer = MediaPlayer()

    override fun getUpdateDelay() : Long {
        return DELAY
    }

    override fun playBackControl() {
        when (playerState) {
            is MediaPlayerState.Playing -> pausePlayer()
            else -> startPlayer()
        }
    }

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)

        mediaPlayer.setOnPreparedListener {
            listener.onPlayerPrepared()
            playerState = MediaPlayerState.Prepared
        }

        mediaPlayer.setOnCompletionListener {
            listener.onPlayerCompletion()

            mediaPlayer.prepare()
            playerState = MediaPlayerState.Prepared
        }

        mediaPlayer.setOnErrorListener { _, _, _ ->
            Toast.makeText(PlaylistApplication.applicationContext, "Не удается загрузить трек из-за проблем при подготовке плеера.", Toast.LENGTH_LONG).show()
            true
        }

        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = MediaPlayerState.Playing

        listener.onPlayerStart()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = MediaPlayerState.Paused

        listener.onPlayerPause()
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }

    override fun getRunnable(): Runnable {
        return Runnable {
            if (playerState == MediaPlayerState.Playing) {
                listener.onPlayerTimerTick()
            }
        }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPrepared(): Boolean {
        return playerState != MediaPlayerState.Default
    }

}