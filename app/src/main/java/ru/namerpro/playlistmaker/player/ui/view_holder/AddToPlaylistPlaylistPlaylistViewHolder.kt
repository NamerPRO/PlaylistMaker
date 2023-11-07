package ru.namerpro.playlistmaker.player.ui.view_holder

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.PlaylistInAddToPlaylistSectionItemBinding
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import java.io.File

class AddToPlaylistPlaylistPlaylistViewHolder(
    private val context: Context,
    private val binding: PlaylistInAddToPlaylistSectionItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        playlistItem: PlaylistModel
    ) {
        if (playlistItem.cover == null) {
            binding.playlistCover.setImageResource(R.drawable.ic_load_failed_stub)
        } else {
            binding.playlistCover.setImageURI(Uri.fromFile(File(playlistItem.cover)))
        }
        binding.playlistTitle.text = playlistItem.title
        binding.playlistTracksCount.text = context.resources.getQuantityString(R.plurals.playlist_item_track_word, playlistItem.tracksIds.size, playlistItem.tracksIds.size)
    }

}