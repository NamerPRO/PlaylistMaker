package ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseInteractor
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.state.PlaylistsAddingState
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.state.PlaylistsStorgeState
import java.io.File
import java.io.FileOutputStream

class PlaylistFragmentViewModel(
    private val playlistsDatabaseInteractor: PlaylistsDatabaseInteractor
): ViewModel() {

    var pickedPlaylistCoverUri: Uri? = null

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
                playlistsDatabaseInteractor.addPlaylist(
                    PlaylistModel(
                        playlistTitle,
                        playlistDescription,
                        savePlaylistCover(playlistTitle, pickedPlaylistCoverUri, context),
                        System.currentTimeMillis(),
                        ArrayList()
                    )
                )
                playlistsAddingStateLiveData.postValue(PlaylistsAddingState.PlaylistAddSuccess(playlistTitle))
            } else {
                playlistsAddingStateLiveData.postValue(PlaylistsAddingState.PlaylistAddError)
            }
        }
    }

    fun deletePlaylist(
        playlist: PlaylistModel
    ) {
        viewModelScope.launch {
            playlistsDatabaseInteractor.deletePlaylist(playlist)
        }
    }

    private fun savePlaylistCover(
        playlistName: String,
        uri: Uri?,
        context: Context
    ): String? {
        if (uri == null) {
            return null
        }

        val filePath = File(context.getExternalFilesDir(null), "")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, playlistName)

        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        val pickedImageAsBitmap = BitmapFactory.decodeStream(inputStream)
        val scaledBitmap = ThumbnailUtils.extractThumbnail(pickedImageAsBitmap, 300, 300)

        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        inputStream?.close()
        outputStream.close()

        return file.absolutePath
    }

}