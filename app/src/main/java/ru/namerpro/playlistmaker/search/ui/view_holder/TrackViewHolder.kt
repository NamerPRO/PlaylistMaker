package ru.namerpro.playlistmaker.search.ui.view_holder

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.TrackItemBinding
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class TrackViewHolder(
    binding: TrackItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val trackName: TextView = binding.trackName
    private val artistName: TextView = binding.artistName
    private val trackTime: TextView = binding.trackTime
    private val trackImage: ImageView = binding.trackImage

    fun bind(
        model: TrackModel
    ) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTimeInFormat

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.ic_load_failed_stub)
            .into(trackImage)
    }

}