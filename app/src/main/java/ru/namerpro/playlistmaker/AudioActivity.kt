package ru.namerpro.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class AudioActivity : AppCompatActivity() {

    fun getCoverArtwork(track: Track) = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        val track : Track = Gson().fromJson(intent.extras!!.getString(TRACK_INTENT_KEY), Track::class.java)

        val audioTrackImage = findViewById<ImageView>(R.id.audio_track_image)
        Glide.with(this)
            .load(getCoverArtwork(track))
            .centerCrop()
            .placeholder(R.drawable.ic_track_no_iternet)
            .into(audioTrackImage)

        val audioTrackName = findViewById<TextView>(R.id.audio_track_name)
        audioTrackName.text = track.trackName

        val audioTrackAuthor = findViewById<TextView>(R.id.audio_track_author)
        audioTrackAuthor.text = track.artistName

        val audioDuration = findViewById<TextView>(R.id.audio_duration_data)
        audioDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        val audioAlbum = findViewById<TextView>(R.id.audio_album_data)
        if (!track.collectionName.isNullOrEmpty()) {
            audioAlbum.text = track.collectionName
        } else {
            audioAlbum.visibility = View.GONE
            findViewById<TextView>(R.id.audio_album).visibility = View.GONE
        }

        val audioYear = findViewById<TextView>(R.id.audio_year_data)
        audioYear.text = track.releaseDate.substringBefore('-')

        val audioGenre = findViewById<TextView>(R.id.audio_genre_data)
        audioGenre.text = track.primaryGenreName

        val audioCountry = findViewById<TextView>(R.id.audio_country_data)
        audioCountry.text = track.country

        val audioBackButton = findViewById<ImageView>(R.id.audio_back_button)
        audioBackButton.setOnClickListener {
            finish()
        }

        val audioTrackProgress = findViewById<TextView>(R.id.audio_track_progress)
        audioTrackProgress.text = "0:30" // ToDo("Реализовать в следующих спринтах")
    }

}