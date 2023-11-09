package ru.namerpro.playlistmaker.playlist.ui.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseInteractor
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import java.io.File

class PlaylistFragmentViewModel(
    private val application: Application,
    private val playlistDatabaseInteractor: PlaylistsDatabaseInteractor,
    private val tracksInPlaylistDatabaseInteractor: TracksInPlaylistDatabaseInteractor
) : ViewModel() {

    var playlist: PlaylistModel? = null

    var tracks: List<TrackModel>? = null

    private val tracksLiveData = MutableLiveData<List<TrackModel>>()
    fun observerTracks(): LiveData<List<TrackModel>> = tracksLiveData

    fun trackIdsToTracks(
        trackIds: List<Long>
    ) {
        viewModelScope.launch {
            val tracks = trackIds.map { id -> tracksInPlaylistDatabaseInteractor.getTrackFromStorage(id) }
                .toList()
                .reversed()
            tracksLiveData.postValue(ArrayList(tracks))
        }
    }

    fun deletePlaylist(
        playlistToDelete: PlaylistModel,
        tracks: List<TrackModel>,
        onPlaylistDeleted: () -> Unit
    ) {
        viewModelScope.launch {
            val fullCoverOfDeletablePlaylist = playlistToDelete.coverFull
            val cutCoverOfDeletablePlaylist = playlistToDelete.coverCut

            if (fullCoverOfDeletablePlaylist != null) {
                File(fullCoverOfDeletablePlaylist).delete()
                File(cutCoverOfDeletablePlaylist!!).delete()
            }

            tracks.forEach { track ->
                deleteTrack(playlistToDelete.title, track, playlistToDelete.tracksIds)
            }

            playlistDatabaseInteractor.deletePlaylist(playlistToDelete)

            onPlaylistDeleted.invoke()
        }
    }

    fun deleteTrackFromPlaylist(
        playlistTitle: String,
        trackToDelete: TrackModel,
        trackIds: ArrayList<Long>
    ) {
        viewModelScope.launch {
            deleteTrack(playlistTitle, trackToDelete, trackIds)
        }
    }

    private suspend fun deleteTrack(
        playlistTitle: String,
        trackToDelete: TrackModel,
        trackIds: ArrayList<Long>
    ) {
        trackIds.remove(trackToDelete.trackId)
        playlistDatabaseInteractor.updateTracksInPlaylist(playlistTitle, trackIds)

        playlistDatabaseInteractor.deleteTrackIfItDoesNotExistInAnyOfPlaylists(trackToDelete)
    }

    fun getAmountAndTotalTracksTimeInPlaylistInMinutes(
        playlist: PlaylistModel?,
        tracks: List<TrackModel>?
    ): Pair<Int, Int> {
        var amountOfTracksInPlaylist = playlist?.tracksIds?.size

        if (amountOfTracksInPlaylist == null) {
            amountOfTracksInPlaylist = 0
        }

        val totalTimeLengthOfTracks = tracks!!.sumOf { track ->
            val timeUnits = track.trackTimeInFormat.split(':')
            timeUnits[0].toInt() + timeUnits[1].toInt() / 60.0
        }.toInt()

        return amountOfTracksInPlaylist to totalTimeLengthOfTracks
    }

    fun playlistToShareableString(
        playlist: PlaylistModel,
        tracks: List<TrackModel>
    ): String {
        var playlistDescription = playlist.description

        if (playlistDescription.isNullOrEmpty()) {
            playlistDescription = "описание отсутствует"
        }

        var shareableString = """
            ${application.getString(R.string.playlist_share_playlist_title)}:
            ${playlist.title}
            
            ${application.getString(R.string.playlist_share_playlist_description)}:
            $playlistDescription
            
            ${application.resources.getQuantityString(
                R.plurals.playlist_item_track_word,
                playlist.tracksIds.size,
                playlist.tracksIds.size
            )}
            =====
        """.trimIndent()

        tracks.forEachIndexed { trackNumber, track ->
            shareableString += "\n" + (trackNumber + 1) + ". " + track.artistName + " - " + track.trackName + " (" + track.trackTimeInFormat + ")"
        }

        return "$shareableString\n====="
    }

}