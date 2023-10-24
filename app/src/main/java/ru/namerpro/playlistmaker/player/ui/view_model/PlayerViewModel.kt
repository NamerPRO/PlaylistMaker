package ru.namerpro.playlistmaker.player.ui.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.*
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerListener
import ru.namerpro.playlistmaker.player.ui.activity.PlayerUpdateState
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.view_model.SearchViewModel
import java.text.SimpleDateFormat
import java.util.*

class PlayerViewModel(
    intent: Intent,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel(), MediaPlayerListener {

    init {
        mediaPlayerInteractor.setListener(this)
    }

    private val playerChangeLiveData = MutableLiveData<PlayerUpdateState>(PlayerUpdateState.Default())
    fun observePlayerChange(): LiveData<PlayerUpdateState> = playerChangeLiveData

    val track: TrackModel = Gson().fromJson(intent.extras!!.getString(SearchViewModel.TRACK_INTENT_KEY), TrackModel::class.java)

    private var playerUpdateTimerJob: Job? = null

    var isPlayerPrepared = false

    fun getCoverArtwork(
        track: TrackModel
    ) = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun startStopPlayer() {
        mediaPlayerInteractor.playBackControl()
    }

    fun startPlayer(
        callCallback: Boolean = true
    ) {
        mediaPlayerInteractor.startPlayer(callCallback)
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }

    fun destroyPlayer() {
        mediaPlayerInteractor.destroyPlayer()
    }

    fun preparePlayer() {
        mediaPlayerInteractor.preparePlayer(track.previewUrl)
    }

    private fun startPlayerTimer() {
        playerUpdateTimerJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isPlaying()) {
                delay(mediaPlayerInteractor.getUpdateDelay())
                playerChangeLiveData.postValue(PlayerUpdateState.Playing(getCurrentTime()))
            }
        }
    }

    fun getCurrentTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayerInteractor.getCurrentPosition())
    }

    fun isPlayerInPreparedState(): Boolean {
        return mediaPlayerInteractor.isPrepared()
    }

    override fun onCleared() {
        super.onCleared()
        destroyPlayer()
    }

    // Media player listeners

    override fun onPlayerCompletion() {
        playerUpdateTimerJob?.cancel()
    }

    override fun onPlayerStart() {
        startPlayerTimer()
    }

    override fun onPlayerPause() {
        playerUpdateTimerJob?.cancel()
        playerChangeLiveData.postValue(PlayerUpdateState.Paused(getCurrentTime()))
    }

    override fun onPlayerPrepared() {
        playerChangeLiveData.postValue(PlayerUpdateState.Prepared())
    }

    // ===

}