package ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_holder

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.PlaylistItemBinding
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import java.io.File

class PlaylistViewHolder(
    private val context: Context,
    private val binding: PlaylistItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        playlistItem: PlaylistModel
    ) {
        if (playlistItem.coverCut == null) {
            binding.playlistListItemPlaylistCover.setImageResource(R.drawable.ic_load_failed_stub)
        } else {
            binding.playlistListItemPlaylistCover.setImageURI(Uri.fromFile(File(playlistItem.coverCut)))
        }

        binding.playlistListItemPlaylistTitle.text = playlistItem.title
        binding.playlistListItemPlaylistTitle.postDelayed({ binding.playlistListItemPlaylistTitle.isSelected = true }, 1500L)

        binding.playlistListItemPlaylistTracksInPlaylist.text = context.resources.getQuantityString(R.plurals.playlist_item_track_word, playlistItem.tracksIds.size, playlistItem.tracksIds.size)
    }

}