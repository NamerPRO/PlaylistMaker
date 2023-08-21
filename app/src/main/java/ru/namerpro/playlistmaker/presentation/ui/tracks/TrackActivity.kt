package ru.namerpro.playlistmaker.presentation.ui.tracks

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import ru.namerpro.playlistmaker.Creator
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.presentation.presenters.MediaPlayerListener
import ru.namerpro.playlistmaker.domain.models.Track
import ru.namerpro.playlistmaker.presentation.ui.search.SearchActivity.Companion.TRACK_INTENT_KEY
import java.text.SimpleDateFormat
import java.util.*

class TrackActivity : AppCompatActivity(), MediaPlayerListener {

    private val mediaPlayer = Creator.provideMediaPlayerInteractor(this)

    private var mainThreadHandler: Handler? = null
    private lateinit var play: ImageView
    private lateinit var previewUrl: String

    // MediaPlayer listeners
    override fun onPlayerCompletion() {
        play.setImageResource(R.drawable.ic_audio_play_button)
        findViewById<TextView>(R.id.audio_track_progress).text = "00:00"
    }

    override fun onPlayerStart() {
        play.setImageResource(R.drawable.ic_audio_pause_button)
        mainThreadHandler?.postDelayed(mediaPlayer.getRunnable(), mediaPlayer.getUpdateDelay())
    }

    override fun onPlayerPause() {
        play.setImageResource(R.drawable.ic_audio_play_button)
    }

    override fun onPlayerTimerTick() {
        val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition())
        findViewById<TextView>(R.id.audio_track_progress).text = currentPosition
        mainThreadHandler?.postDelayed(mediaPlayer.getRunnable(), mediaPlayer.getUpdateDelay())
    }
    // ===

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track : Track = Gson().fromJson(intent.extras!!.getString(TRACK_INTENT_KEY), Track::class.java)

        play = findViewById(R.id.audio_play_button)

        previewUrl = track.previewUrl
        mediaPlayer.preparePlayer(track.previewUrl)

        play.setOnClickListener {
            mediaPlayer.playBackControl()
        }

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
        audioDuration.text = track.trackTimeInFormat

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
        audioTrackProgress.text = "0:30"
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.destroyPlayer()
    }

    private fun getCoverArtwork(track: Track) = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

}