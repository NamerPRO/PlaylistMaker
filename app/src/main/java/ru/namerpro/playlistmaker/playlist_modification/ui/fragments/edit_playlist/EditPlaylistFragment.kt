package ru.namerpro.playlistmaker.playlist_modification.ui.fragments.edit_playlist

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.common.utils.showToast
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.playlist.ui.fragment.PlaylistFragment
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.create_playlist.CreatePlaylistFragment
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.edit_playlist.state.PlaylistUpdateState
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.edit_playlist.view_model.EditPlaylistViewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    override val viewModel: EditPlaylistViewModel by viewModel()

    private var playlist: PlaylistModel? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        playlist = Gson().fromJson(requireArguments().getString(PLAYLIST_TO_EDIT), PlaylistModel::class.java)

        binding?.playlistCreateOrEditTitle?.text = getString(R.string.playlist_create_playlist_edit)
        binding?.newPlaylistCreatePlaylistButton?.text = getString(R.string.new_playlist_edit_playlist_button_text)

        binding?.newPlaylistPlaylistTitleEdittext?.setText(playlist?.title)

        binding?.newPlaylistPlaylistDescriptionEdittext?.setText(playlist?.description ?: "")

        if (viewModel.pickedDrawable != null || playlist?.coverFull != null) {
            binding?.newPlaylistSelectCover?.scaleType = ImageView.ScaleType.CENTER_CROP
            binding?.newPlaylistSelectCover?.background = null

            if (viewModel.pickedDrawable != null) {
                binding?.newPlaylistSelectCover?.setImageDrawable(viewModel.pickedDrawable)
            } else {
                binding?.newPlaylistSelectCover?.setImageURI(Uri.parse(playlist?.coverFull))
                viewModel.pickedDrawable = binding?.newPlaylistSelectCover?.drawable
            }
        } else {
            binding?.newPlaylistSelectCover?.setImageResource(R.drawable.ic_new_playlist_add_photo)
        }

        viewModel.observePlaylistUpdate().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistUpdateState.UpdatedPlaylist -> {
                    findNavController().popBackStack(R.id.mediaFragment, inclusive = false)

                    findNavController().navigate(
                        R.id.action_mediaFragment_to_playlistFragment,
                        PlaylistFragment.createArgs(state.playlist)
                    )
                }
                is PlaylistUpdateState.PlaylistUpdateError -> {
                    showToast(
                        this,
                        layoutInflater,
                        getString(R.string.edit_playlist_edit_failed_because_another_playlist_with_same_name_exists)
                    )
                }
            }
        }

        binding?.newPlaylistCreatePlaylistButton?.setOnClickListener {
            val playlistTitle = binding?.newPlaylistPlaylistTitleEdittext?.text.toString()
            val playlistDescription = binding?.newPlaylistPlaylistDescriptionEdittext?.text.toString()

            viewModel.updatePlaylist(playlistTitle, playlistDescription, requireContext(), playlist)
        }
    }

    override fun onFragmentCloseLogic() {
        findNavController().navigateUp()
    }

    companion object {
        private const val PLAYLIST_TO_EDIT = "edit_playlist_playlist_to_edit"

        fun createArgs(
            playlist: PlaylistModel
        ): Bundle {
            val playlistAsString = Gson().toJson(playlist)
            return bundleOf(PLAYLIST_TO_EDIT to playlistAsString)
        }
    }

}