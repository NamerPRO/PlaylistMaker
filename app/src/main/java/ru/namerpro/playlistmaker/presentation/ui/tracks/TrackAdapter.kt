package ru.namerpro.playlistmaker.presentation.ui.tracks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.domain.models.Track

class TrackAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((Int, Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position, track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}