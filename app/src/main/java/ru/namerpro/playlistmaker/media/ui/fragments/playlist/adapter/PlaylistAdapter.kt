package ru.namerpro.playlistmaker.media.ui.fragments.playlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.namerpro.playlistmaker.databinding.PlaylistItemBinding
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_holder.PlaylistViewHolder

class PlaylistAdapter(
    val playlists: ArrayList<PlaylistModel>,
    private val itemClickListener: ((PlaylistModel) -> Unit)
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(parent.context, PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

}