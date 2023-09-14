package ru.namerpro.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.TrackItemBinding
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.view_model.TrackViewHolder

class TrackAdapter(
    val tracks: ArrayList<TrackModel>,
    private val itemClickListener: ((TrackModel) -> Unit)
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}