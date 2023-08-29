package ru.namerpro.playlistmaker.player.ui.view_model

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
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

    private val playerChangeLiveData = MutableLiveData<PlayerUpdateState>()
    fun observePlayerChange(): LiveData<PlayerUpdateState> = playerChangeLiveData

    val track: TrackModel = Gson().fromJson(intent.extras!!.getString(SearchViewModel.TRACK_INTENT_KEY), TrackModel::class.java)

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    var isPlayerPrepared = false

    fun getCoverArtwork(
        track: TrackModel
    ) = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun startStopPlayer() {
        mediaPlayerInteractor.playBackControl()
    }

    fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
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

    fun updateProgress() {
        mainThreadHandler.postDelayed(mediaPlayerInteractor.getRunnable(), mediaPlayerInteractor.getUpdateDelay())
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
        playerChangeLiveData.postValue(PlayerUpdateState.Completion)
    }

    override fun onPlayerStart() {
        playerChangeLiveData.postValue(PlayerUpdateState.Start)
    }

    override fun onPlayerPause() {
        playerChangeLiveData.postValue(PlayerUpdateState.Pause)
    }

    override fun onPlayerTimerTick() {
        playerChangeLiveData.postValue(PlayerUpdateState.Tick)
    }

    override fun onPlayerPrepared() {
        playerChangeLiveData.postValue(PlayerUpdateState.Prepared)
    }

    // ===

}