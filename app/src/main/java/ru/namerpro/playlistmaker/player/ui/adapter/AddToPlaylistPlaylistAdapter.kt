package ru.namerpro.playlistmaker.player.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.namerpro.playlistmaker.databinding.PlaylistInAddToPlaylistSectionItemBinding
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.player.ui.view_holder.AddToPlaylistPlaylistPlaylistViewHolder

class AddToPlaylistPlaylistAdapter(
    var playlists: ArrayList<PlaylistModel>,
    private val onItemClickListener: (playlist: PlaylistModel) -> Unit
) : RecyclerView.Adapter<AddToPlaylistPlaylistPlaylistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddToPlaylistPlaylistPlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AddToPlaylistPlaylistPlaylistViewHolder(parent.context, PlaylistInAddToPlaylistSectionItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: AddToPlaylistPlaylistPlaylistViewHolder,
        position: Int
    ) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

}