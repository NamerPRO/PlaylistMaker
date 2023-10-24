package ru.namerpro.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.ActivityAudioBinding
import ru.namerpro.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private lateinit var play: ImageView
    private lateinit var audioTrackImage: ImageView
    private lateinit var audioTrackName: TextView
    private lateinit var audioTrackAuthor: TextView
    private lateinit var audioDuration: TextView
    private lateinit var audioAlbumData: TextView
    private lateinit var audioYear: TextView
    private lateinit var audioGenre: TextView
    private lateinit var audioCountry: TextView
    private lateinit var audioBackButton: ImageView
    private lateinit var audioTrackProgress: TextView
    private lateinit var audioAlbum: TextView

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(intent)
    }

    private lateinit var binding: ActivityAudioBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioTrackImage = binding.audioTrackImage
        audioTrackName = binding.audioTrackName
        audioTrackAuthor = binding.audioTrackAuthor
        audioDuration = binding.audioDurationData
        audioAlbumData = binding.audioAlbumData
        audioAlbum = binding.audioAlbum
        audioYear = binding.audioYearData
        audioGenre = binding.audioGenreData
        audioCountry = binding.audioCountryData
        audioBackButton = binding.audioBackButton
        audioTrackProgress = binding.audioTrackProgress
        play = binding.audioPlayButton

        setTrackInfo()

        if (!viewModel.isPlayerPrepared) {
            viewModel.preparePlayer()
            viewModel.isPlayerPrepared = true
        }

        viewModel.observePlayerChange().observe(this) { playerState ->
            play.isEnabled = playerState.isPlayButtonEnabled
            when (playerState) {
                is PlayerUpdateState.Playing -> play.setImageResource(R.drawable.ic_audio_pause_button)
                else -> play.setImageResource(R.drawable.ic_audio_play_button)
            }
            audioTrackProgress.text = playerState.progress
        }

        play.setOnClickListener {
            viewModel.startStopPlayer()
        }

        audioBackButton.setOnClickListener {
            finish()
        }
    }

    private fun setTrackInfo() {
        Glide.with(this)
            .load(viewModel.getCoverArtwork(viewModel.track))
            .centerCrop()
            .placeholder(R.drawable.ic_track_no_iternet)
            .into(audioTrackImage)

        audioTrackName.text = viewModel.track.trackName
        audioTrackAuthor.text = viewModel.track.artistName
        audioDuration.text = viewModel.track.trackTimeInFormat
        audioYear.text = viewModel.track.releaseDate.substringBefore('-')
        audioGenre.text = viewModel.track.primaryGenreName
        audioCountry.text = viewModel.track.country
        audioTrackProgress.text = if (viewModel.isPlayerPrepared) viewModel.getCurrentTime() else "0:30"

        if (!viewModel.track.collectionName.isNullOrEmpty()) {
            audioAlbumData.text = viewModel.track.collectionName
        } else {
            audioAlbumData.visibility = View.GONE
            audioAlbum.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.isPlayerInPreparedState()) {
            viewModel.startPlayer(false)
            viewModel.pausePlayer()
        }
    }

}