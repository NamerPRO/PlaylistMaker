package ru.namerpro.playlistmaker.search.ui.view_model

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: TrackModel) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTimeInFormat

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.ic_track_no_iternet)
            .into(trackImage)
    }

}