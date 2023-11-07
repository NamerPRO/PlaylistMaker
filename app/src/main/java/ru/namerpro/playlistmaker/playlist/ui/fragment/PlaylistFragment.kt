package ru.namerpro.playlistmaker.playlist.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.text.italic
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.common.utils.Dp
import ru.namerpro.playlistmaker.databinding.FragmentPlaylistBinding
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.player.ui.fragment.PlayerFragment
import ru.namerpro.playlistmaker.playlist.ui.view_model.PlaylistFragmentViewModel
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.edit_playlist.EditPlaylistFragment
import ru.namerpro.playlistmaker.search.ui.adapter.TrackAdapter

class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistFragmentViewModel by viewModel()

    private var binding: FragmentPlaylistBinding? = null

    private lateinit var dp: Dp

    private var extraSettingsMenuBehaviour: BottomSheetBehavior<CardView>? = null

    private val tracksInPlaylistAdapter = TrackAdapter(ArrayList()) { track, isClickLong ->
        if (isClickLong) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlist_delete_playlist_from_list))
                .setPositiveButton(getString(R.string.dialog_yes_button)) { _, _ ->
                    viewModel.deleteTrackFromPlaylist(viewModel.playlist!!.title, track, viewModel.playlist!!.tracksIds)
                    (viewModel.tracks as ArrayList).remove(track)
                    viewModel.playlist?.tracksIds?.remove(track.trackId)
                    fillListWithPlaylists()
                    updateTrackLength()
                }
                .setNegativeButton(getString(R.string.dialog_no_button)) { _, _ -> }
                .show()
        } else {
            findNavController().navigate(
                R.id.action_playlistFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        dp = Dp(resources)

        if (viewModel.playlist == null) {
            viewModel.playlist = Gson().fromJson(arguments?.getString(PLAYLIST_KEY), PlaylistModel::class.java)
        }

        if (viewModel.tracks == null) {
            viewModel.playlist?.tracksIds?.let { viewModel.trackIdsToTracks(it) }
        }

        binding?.playlistPlaylistTitle?.text = viewModel.playlist?.title

        if (viewModel.playlist?.description.isNullOrEmpty()) {
            binding?.playlistPlaylistDescription?.text = SpannableStringBuilder().italic { append("описание не указано") }
        } else {
            binding?.playlistPlaylistDescription?.text = viewModel.playlist?.description
        }

        binding?.playlistTracksInPlaylist?.apply {
            adapter = tracksInPlaylistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.observerTracks().observe(viewLifecycleOwner) { tracks ->
            viewModel.tracks = tracks

            updateTrackLength()
            fillListWithPlaylists()
        }

        if (viewModel.playlist?.coverFull != null) {
            binding?.playlistPlaylistCover?.setImageURI(Uri.parse(viewModel.playlist?.coverFull))
        } else {
            val layoutParams = binding?.playlistPlaylistCover?.layoutParams as? ViewGroup.MarginLayoutParams

            layoutParams?.setMargins(0, dp.of(63), 0, dp.of(63))
            layoutParams?.width = dp.of(236)
            layoutParams?.height = dp.of(236)

            binding?.playlistPlaylistCover?.setImageResource(R.drawable.ic_load_failed_stub)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            binding?.playlistPlaylistTitle?.isSelected = true
            binding?.playlistPlaylistDescription?.isSelected = true
        }, 1000L)

        binding?.playlistBackButton?.setOnClickListener {
            findNavController().navigateUp()
        }

        view.doOnNextLayout {
            calculatePeekHeightTracks()
        }

        binding?.playlistPlaylistShare?.setOnClickListener {
            sharePlaylist()
        }

        extraSettingsMenuBehaviour = BottomSheetBehavior.from(binding!!.playlistExtraSettingsMenu)
        extraSettingsMenuBehaviour?.state = BottomSheetBehavior.STATE_HIDDEN

        binding?.playlistPlaylistExtraSettings?.setOnClickListener {
            calculatePeekHeightExtraSettings()
            extraSettingsMenuBehaviour?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding?.playlistExtraSettingsMenuShareButton?.setOnClickListener {
            sharePlaylist()
            extraSettingsMenuBehaviour?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        if (viewModel.playlist?.coverCut != null) {
            binding?.playlistCover?.setImageURI(Uri.parse(viewModel.playlist?.coverCut))
        } else {
            binding?.playlistCover?.setImageResource(R.drawable.ic_load_failed_stub)
        }

        binding?.playlistTitle?.text = viewModel.playlist?.title

        val amountOfTracks = viewModel.playlist!!.tracksIds.size
        binding?.playlistTracksCount?.text = resources.getQuantityString(R.plurals.playlist_item_track_word, amountOfTracks, amountOfTracks)

        binding?.playlistExtraSettingsMenuDeletePlaylistButton?.setOnClickListener {
            extraSettingsMenuBehaviour?.state = BottomSheetBehavior.STATE_HIDDEN
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlist_delete_playlist_alert_title))
                .setMessage(getString(R.string.playlist_extra_settings_playlist_delete_confirmation, viewModel.playlist?.title))
                .setPositiveButton(getString(R.string.dialog_yes_button)) { _, _ ->
                    viewModel.deletePlaylist(viewModel.playlist!!, viewModel.tracks!!) {
                        findNavController().navigateUp()
                    }
                }
                .setNegativeButton(getString(R.string.dialog_no_button)) { _, _ -> }
                .show()
        }

        binding?.playlistExtraSettingsEditPlaylist?.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(viewModel.playlist!!)
            )
        }
    }

    private fun sharePlaylist() {
        if (viewModel.tracks.isNullOrEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.playlist_share_empty_playlist_title)
                .setMessage(getString(R.string.no_tracks_in_playlist_to_share_button))
                .setPositiveButton(getString(R.string.dialog_ok_button)) { _, _ -> }
                .show()
        } else {
            val shareableString = viewModel.playlistToShareableString(viewModel.playlist!!, viewModel.tracks!!)
            val sharePlaylistIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.playlist_share_subject, viewModel.playlist?.title))
                putExtra(Intent.EXTRA_TEXT, shareableString)
            }
            startActivity(sharePlaylistIntent)
        }
    }

    private fun updateTrackLength() {
        val tracksAmountAndTotalTime = viewModel.getAmountAndTotalTracksTimeInPlaylistInMinutes(viewModel.playlist, viewModel.tracks)

        binding?.playlistPlaylistInformation?.text = resources.getQuantityString(
            R.plurals.playlist_playlist_information_time,
            tracksAmountAndTotalTime.second,
            tracksAmountAndTotalTime.second
        ).plus(resources.getQuantityString(
            R.plurals.playlist_playlist_information_tracks_count,
            tracksAmountAndTotalTime.first,
            tracksAmountAndTotalTime.first
        ))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fillListWithPlaylists() {
        if (viewModel.tracks?.isEmpty() == false) {
            binding?.playlistTracksInPlaylist?.isVisible = true
            binding?.playlistNoTracksInPlaylist?.isVisible = false

            tracksInPlaylistAdapter.tracks.clear()
            tracksInPlaylistAdapter.tracks.addAll(viewModel.tracks!!)
            tracksInPlaylistAdapter.notifyDataSetChanged()
        } else {
            binding?.playlistTracksInPlaylist?.isVisible = false
            binding?.playlistNoTracksInPlaylist?.isVisible = true
        }
    }

    private fun calculatePeekHeightTracks() {
        val screenHeight = binding!!.root.height
        val bottomSheetBehavior = BottomSheetBehavior.from(binding!!.playlistTracksInPlaylistMenu)
        bottomSheetBehavior.peekHeight = screenHeight - binding!!.playlistPlaylistData.height - dp.of(24)
    }

    private fun calculatePeekHeightExtraSettings() {
        val screenHeight = binding!!.root.height
        extraSettingsMenuBehaviour?.peekHeight = screenHeight - binding!!.playlistPlaylistDescription.y.toInt()
    }

    companion object {
        private const val PLAYLIST_KEY = "playlist_key"

        fun createArgs(playlist: PlaylistModel): Bundle {
            val playlistAsString = Gson().toJson(playlist)
            return bundleOf(PLAYLIST_KEY to playlistAsString)
        }
    }

}