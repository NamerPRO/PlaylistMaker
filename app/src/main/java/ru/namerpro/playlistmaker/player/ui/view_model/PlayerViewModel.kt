package ru.namerpro.playlistmaker.player.ui.view_model

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.namerpro.playlistmaker.common.domain.api.FavouritesDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseInteractor
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerListener
import ru.namerpro.playlistmaker.player.ui.fragment.state.AddToPlaylistState
import ru.namerpro.playlistmaker.player.ui.fragment.state.PlayerUpdateState
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlayerViewModel(
    val track: TrackModel,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favouritesDatabaseInteractor: FavouritesDatabaseInteractor,
    private val playlistsDatabaseInteractor: PlaylistsDatabaseInteractor,
    private val tracksInPlaylistDatabaseInteractor: TracksInPlaylistDatabaseInteractor
) : ViewModel(), MediaPlayerListener {

    init {
        mediaPlayerInteractor.setListener(this)
    }

    private val playerChangeLiveData = MutableLiveData<PlayerUpdateState>(PlayerUpdateState.Default())
    fun observePlayerChange(): LiveData<PlayerUpdateState> = playerChangeLiveData

    private val favouritesChangeLiveData = MutableLiveData<Boolean>()
    fun observeFavouritesChange(): LiveData<Boolean> = favouritesChangeLiveData

    private val addToPlaylistLiveData = MutableLiveData<AddToPlaylistState>()
    fun observerAddToPlaylistLiveDate(): LiveData<AddToPlaylistState> = addToPlaylistLiveData

    private var playerUpdateTimerJob: Job? = null

    var isPlayerPrepared = false

    fun getCoverArtwork(
        track: TrackModel
    ) = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun setFavouritesButton() {
        viewModelScope.launch {
            favouritesChangeLiveData.postValue(favouritesDatabaseInteractor.isTrackFavourite(track.trackId))
        }
    }

    fun onAddToPlaylistClick() {
        viewModelScope.launch {
            val playlists = playlistsDatabaseInteractor.getPlaylist()
            addToPlaylistLiveData.postValue(AddToPlaylistState.WithPlaylists(playlists))
        }
    }

    fun addTrackToPlaylist(
        playlistTitle: String,
        tracksIds: ArrayList<Long>
    ) {
        viewModelScope.launch {
            if (!tracksInPlaylistDatabaseInteractor.isInTrackInPlaylistStorage(track.trackId)) {
                tracksInPlaylistDatabaseInteractor.addToTrackInPlaylistStorage(track, System.currentTimeMillis())
            }
            playlistsDatabaseInteractor.updateTracksInPlaylist(playlistTitle, tracksIds)
        }
    }

    fun onFavouriteClicked() {
        viewModelScope.launch {
            if (!favouritesDatabaseInteractor.isTrackFavourite(track.trackId)) {
                favouritesDatabaseInteractor.addToFavourites(track, System.currentTimeMillis())
                favouritesChangeLiveData.postValue(true)
            } else {
                favouritesDatabaseInteractor.deleteFromFavourites(track)
                favouritesChangeLiveData.postValue(false)
            }
        }
    }

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