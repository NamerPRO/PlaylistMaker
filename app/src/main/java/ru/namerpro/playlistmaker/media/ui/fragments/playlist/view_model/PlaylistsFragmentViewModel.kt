package ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseInteractor
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.create_playlist.state.PlaylistsAddingState
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.state.PlaylistsStorgeState
import java.io.File

open class PlaylistsFragmentViewModel(
    private val playlistsDatabaseInteractor: PlaylistsDatabaseInteractor
): ViewModel() {

    var pickedDrawable: Drawable? = null

    private val playlistsStorageLiveData = MutableLiveData<PlaylistsStorgeState>()
    fun observePlaylistsStorageLiveData(): LiveData<PlaylistsStorgeState> = playlistsStorageLiveData

    private val playlistsAddingStateLiveData = MutableLiveData<PlaylistsAddingState>()
    fun observePlaylistsAddingStateLiveData(): LiveData<PlaylistsAddingState> = playlistsAddingStateLiveData

    fun getPlaylists() {
        viewModelScope.launch {
            val playlists = playlistsDatabaseInteractor.getPlaylist()
            if (playlists.isEmpty()) {
                playlistsStorageLiveData.postValue(PlaylistsStorgeState.Empty)
            } else {
                playlistsStorageLiveData.postValue(PlaylistsStorgeState.WithPlaylists(playlists))
            }
        }
    }

    fun addPlaylist(
        playlistTitle: String,
        playlistDescription: String,
        context: Context
    ) {
        viewModelScope.launch {
            if (!playlistsDatabaseInteractor.hasPlaylist(playlistTitle)) {
                val savedFullAndCutCovers = savePlaylistCover(playlistTitle, context)
                val playlist = PlaylistModel(
                    0,
                    playlistTitle,
                    playlistDescription,
                    savedFullAndCutCovers?.first,
                    savedFullAndCutCovers?.second,
                    System.currentTimeMillis(),
                    ArrayList()
                )
                playlistsDatabaseInteractor.addPlaylist(playlist)
                playlistsAddingStateLiveData.postValue(PlaylistsAddingState.PlaylistAddSuccess(playlist))
            } else {
                playlistsAddingStateLiveData.postValue(PlaylistsAddingState.PlaylistAddError)
            }
        }
    }

    protected fun savePlaylistCover(
        playlistName: String,
        context: Context
    ): Pair<String, String>? {
        if (pickedDrawable == null) {
            return null
        }

        val filePath = File(context.getExternalFilesDir(null), "")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val fileFull = File(filePath, playlistName + "_full")
        val fileCut = File(filePath, playlistName)

        val outputStreamFull = fileFull.outputStream()
        val outputStreamCut = fileCut.outputStream()

        val pickedImageAsBitmap = (pickedDrawable as BitmapDrawable).bitmap
        val scaledBitmap = ThumbnailUtils.extractThumbnail(pickedImageAsBitmap, 300, 300)

        pickedImageAsBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStreamFull)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStreamCut)

        outputStreamCut.close()
        outputStreamFull.close()

        return Pair(fileFull.absolutePath, fileCut.absolutePath)
    }

}