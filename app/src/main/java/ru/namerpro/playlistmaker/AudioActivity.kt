package ru.namerpro.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class AudioActivity : AppCompatActivity() {

    companion object {
        private const val STATUS_DEFAULT = 0
        private const val STATUS_PREPARED = 1
        private const val STATUS_PLAYING = 2
        private const val STATATUS_PAUSED = 3

        private const val DELAY = 300L
    }

    private var mainThreadHandler: Handler? = null

    private var playerState = STATUS_DEFAULT

    private lateinit var play: ImageView
    private var mediaPlayer = MediaPlayer()

    private lateinit var previewUrl: String

    fun getCoverArtwork(track: Track) = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track : Track = Gson().fromJson(intent.extras!!.getString(TRACK_INTENT_KEY), Track::class.java)

        play = findViewById(R.id.audio_play_button)

        previewUrl = track.previewUrl
        preparePlayer()

        play.setOnClickListener {
            playBackControl()
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
        audioTrackProgress.text = "0:30"
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playBackControl() {
        when (playerState) {
            STATUS_PLAYING -> {
                pausePlayer()
            }
            STATUS_PREPARED, STATATUS_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        mainThreadHandler?.post(createUpdateTimerTask())
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATUS_PLAYING) {
                    val currentPosition = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    findViewById<TextView>(R.id.audio_track_progress).text = currentPosition
                    mainThreadHandler?.postDelayed(this, DELAY)
                } else {
                    mainThreadHandler?.removeCallbacks(this)
                }
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATUS_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            play.setImageResource(R.drawable.ic_audio_play_button)
            playerState = STATUS_PREPARED
            findViewById<TextView>(R.id.audio_track_progress).text = "00:00"
            mediaPlayer.reset()
            preparePlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        play.setImageResource(R.drawable.ic_audio_pause_button)
        playerState = STATUS_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageResource(R.drawable.ic_audio_play_button)
        playerState = STATATUS_PAUSED
    }

}