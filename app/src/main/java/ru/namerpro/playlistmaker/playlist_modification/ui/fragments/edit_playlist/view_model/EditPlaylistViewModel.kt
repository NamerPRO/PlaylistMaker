package ru.namerpro.playlistmaker.playlist_modification.ui.fragments.edit_playlist.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseInteractor
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model.PlaylistsFragmentViewModel
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.edit_playlist.state.PlaylistUpdateState
import java.io.File

class EditPlaylistViewModel(
    private val playlistsDatabaseInteractor: PlaylistsDatabaseInteractor
) : PlaylistsFragmentViewModel(playlistsDatabaseInteractor) {

    private val playlistUpdateLiveData = MutableLiveData<PlaylistUpdateState>()
    fun observePlaylistUpdate(): LiveData<PlaylistUpdateState> = playlistUpdateLiveData

    fun updatePlaylist(
        playlistTitle: String,
        playlistDescription: String,
        context: Context,
        playlist: PlaylistModel?
    ) {
        viewModelScope.launch {
            if (playlistTitle == playlist?.title || !playlistsDatabaseInteractor.hasPlaylist(playlistTitle)) {
                if (playlist?.coverFull != null) {
                    val previousCoverFull = File(playlist.coverFull)
                    val previousCoverCut = File(playlist.coverCut!!)

                    previousCoverFull.delete()
                    previousCoverCut.delete()
                }

                val savedFullAndCutCovers = savePlaylistCover(playlistTitle, context)
                val newPlaylist = PlaylistModel(
                    playlist!!.id,
                    playlistTitle,
                    playlistDescription,
                    savedFullAndCutCovers?.first,
                    savedFullAndCutCovers?.second,
                    System.currentTimeMillis(),
                    playlist.tracksIds
                )

                playlistsDatabaseInteractor.updatePlaylist(newPlaylist)
                playlistUpdateLiveData.postValue(PlaylistUpdateState.UpdatedPlaylist(newPlaylist))
            } else {
                playlistUpdateLiveData.postValue(PlaylistUpdateState.PlaylistUpdateError)
            }
        }
    }

}