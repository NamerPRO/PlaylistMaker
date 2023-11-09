package ru.namerpro.playlistmaker.player.ui.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.common.utils.showToast
import ru.namerpro.playlistmaker.databinding.FragmentPlayerBinding
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.create_playlist.CreatePlaylistFragment
import ru.namerpro.playlistmaker.player.ui.adapter.AddToPlaylistPlaylistAdapter
import ru.namerpro.playlistmaker.player.ui.fragment.state.AddToPlaylistState
import ru.namerpro.playlistmaker.player.ui.fragment.state.PlayerUpdateState
import ru.namerpro.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class PlayerFragment : Fragment() {

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
    private lateinit var audioFavoriteButton: ImageView
    private lateinit var bottomSheetContainer: CardView
    private lateinit var audioAddButton: FloatingActionButton
    private lateinit var addToPlaylistButton: Button

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(Gson().fromJson(requireArguments().getString(ARGS_TRACK), object : TypeToken<TrackModel>() {}.type))
    }

    private lateinit var binding: FragmentPlayerBinding

    private val addToPlaylistAdapter = AddToPlaylistPlaylistAdapter(ArrayList()) { playlist ->
        if (!playlist.tracksIds.contains(viewModel.track.trackId)) {
            playlist.tracksIds.add(viewModel.track.trackId)
            viewModel.addTrackToPlaylist(playlist.title, playlist.tracksIds)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            onBottomSheetHide()
            showToast(
                this,
                layoutInflater,
                getString(
                    R.string.add_to_playlist_successfully_added_track,
                    playlist.title
                )
            )
        } else {
            showToast(
                this,
                layoutInflater,
                getString(
                    R.string.add_to_playlist_failed_due_to_already_exists,
                    playlist.title
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

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
        audioFavoriteButton = binding.audioFavouriteButton
        bottomSheetContainer = binding.addToPlaylistBottomSheetContainer
        audioAddButton = binding.audioAddButton
        addToPlaylistButton = binding.addToPlaylistButton

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        setTrackInfo()

        if (!viewModel.isPlayerPrepared) {
            viewModel.preparePlayer()
            viewModel.isPlayerPrepared = true
        }

        audioFavoriteButton.setOnClickListener {
            viewModel.onFavouriteClicked()
        }

        viewModel.observeFavouritesChange().observe(viewLifecycleOwner) { isFavourite ->
            if (isFavourite) {
                audioFavoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.yp_red))
                audioFavoriteButton.setImageResource(R.drawable.ic_player_favourite_button_clicked)
            } else {
                audioFavoriteButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.yp_white))
                audioFavoriteButton.setImageResource(R.drawable.ic_player_favourite_button)
            }
        }

        viewModel.observePlayerChange().observe(viewLifecycleOwner) { playerState ->
            play.isEnabled = playerState.isPlayButtonEnabled
            when (playerState) {
                is PlayerUpdateState.Playing -> play.setImageResource(R.drawable.ic_player_pause_button)
                else -> play.setImageResource(R.drawable.ic_player_play_button)
            }
            audioTrackProgress.text = playerState.progress
        }

        play.setOnClickListener {
            viewModel.startStopPlayer()
        }

        audioBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        audioAddButton.setOnClickListener {
            viewModel.onAddToPlaylistClick()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(
                bottomSheet: View,
                newState: Int
            ) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> onBottomSheetHide()
                    else -> onBottomSheetShow()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })

        binding.playerScrollableContainer.setOnTouchListener { _, _ ->
            bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addToPlaylistPlaylistsList.apply {
            adapter = addToPlaylistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.observerAddToPlaylistLiveDate().observe(viewLifecycleOwner) { playlistsState ->
            val playlists = (playlistsState as AddToPlaylistState.WithPlaylists).playlists
            addToPlaylistAdapter.playlists.clear()
            addToPlaylistAdapter.playlists.addAll(playlists)
            addToPlaylistAdapter.notifyDataSetChanged()
        }

        addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(
                R.id.action_playerFragment_to_createPlaylistFragment
            )
        }
    }

    private fun onBottomSheetHide() {
        enableDisableViewGroup(binding.playerScrollableContainer, true)
        binding.bottomSheetBackground.isVisible = false
    }

    private fun onBottomSheetShow() {
        enableDisableViewGroup(binding.playerScrollableContainer, false)
        binding.bottomSheetBackground.isVisible = true
    }

    private fun enableDisableViewGroup(viewGroup: ViewGroup, enabled: Boolean) {
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            view.isEnabled = enabled
            if (view is ViewGroup) {
                enableDisableViewGroup(view, enabled)
            }
        }
    }

    private fun setTrackInfo() {
        Glide.with(this)
            .load(viewModel.getCoverArtwork(viewModel.track))
            .centerCrop()
            .placeholder(R.drawable.ic_load_failed_stub)
            .into(audioTrackImage)

        audioTrackName.text = viewModel.track.trackName
        audioTrackAuthor.text = viewModel.track.artistName
        audioDuration.text = viewModel.track.trackTimeInFormat
        audioYear.text = viewModel.track.releaseDate.substringBefore('-')
        audioGenre.text = viewModel.track.primaryGenreName
        audioCountry.text = viewModel.track.country
        audioTrackProgress.text = if (viewModel.isPlayerPrepared) viewModel.getCurrentTime() else "0:30"

        viewModel.setFavouritesButton()

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

    companion object {
        const val ARGS_TRACK = "args_track"

        fun createArgs(track: TrackModel): Bundle {
            val trackAsString = Gson().toJson(track)
            return bundleOf(ARGS_TRACK to trackAsString)
        }
    }

}