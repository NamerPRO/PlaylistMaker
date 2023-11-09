package ru.namerpro.playlistmaker.playlist_modification.ui.fragments.create_playlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.common.utils.showToast
import ru.namerpro.playlistmaker.databinding.FragmentNewPlaylistBinding
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model.PlaylistsFragmentViewModel
import ru.namerpro.playlistmaker.playlist_modification.ui.fragments.create_playlist.state.PlaylistsAddingState

open class CreatePlaylistFragment : Fragment() {

    protected open val viewModel: PlaylistsFragmentViewModel by viewModel()

    protected var binding: FragmentNewPlaylistBinding? = null

    private var wasImageSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding?.newPlaylistBack?.setOnClickListener {
            onFragmentCloseLogic()
        }

        binding?.newPlaylistPlaylistTitleEdittext?.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(line: CharSequence?, start: Int, before: Int, count: Int) {
                binding?.newPlaylistCreatePlaylistButton?.isEnabled = !line.isNullOrEmpty()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}

        })

        val pickPlaylistCover = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val uri = result.data!!.data!!
                wasImageSelected = true
                binding?.newPlaylistSelectCover?.scaleType = ImageView.ScaleType.CENTER_CROP
                binding?.newPlaylistSelectCover?.setImageURI(uri)

                viewModel.pickedDrawable = binding?.newPlaylistSelectCover?.drawable
            }
        }

        binding?.newPlaylistSelectCover?.setOnClickListener {
            pickPlaylistCover.launch(
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                onFragmentCloseLogic()
            }

        })

        binding?.newPlaylistCreatePlaylistButton?.setOnClickListener {
            val playlistTitle = binding?.newPlaylistPlaylistTitleEdittext?.text.toString()
            val playlistDescription = binding?.newPlaylistPlaylistDescriptionEdittext?.text.toString()

            viewModel.addPlaylist(playlistTitle, playlistDescription, requireContext())
        }

        viewModel.observePlaylistsAddingStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsAddingState.PlaylistAddSuccess -> {
                    showToast(
                        requireParentFragment(),
                        layoutInflater,
                        getString(
                            R.string.new_playlist_create_playlist_was_successfully_created,
                            state.playlist.title
                        )
                    )

                    findNavController().navigateUp()
                }
                is PlaylistsAddingState.PlaylistAddError -> {
                    showToast(
                        requireParentFragment(),
                        layoutInflater,
                        getString(
                            R.string.new_playlist_create_playlist_failed_to_create
                        )
                    )
                }
            }
        }

        wasImageSelected = viewModel.pickedDrawable != null

        if (wasImageSelected) {
            binding?.newPlaylistSelectCover?.scaleType = ImageView.ScaleType.CENTER_CROP
            binding?.newPlaylistSelectCover?.setImageDrawable(viewModel.pickedDrawable)
        }
    }

    protected open fun onFragmentCloseLogic() {
        if (binding?.newPlaylistPlaylistTitleEdittext?.text?.isEmpty() != false
                && binding?.newPlaylistPlaylistDescriptionEdittext?.text?.isEmpty() != false
                && !wasImageSelected) {
            findNavController().navigateUp()
            return
        }
        val goBackConfirmationDialog = MaterialAlertDialogBuilder(requireContext(), R.style.alert_theme)
            .setTitle(getString(R.string.dialog_finish_playlist_create_title))
            .setMessage(getString(R.string.dialog_finish_playlist_create_warning))
            .setNeutralButton(getString(R.string.dialog_cancel_button)) { _, _ -> }
            .setNegativeButton(getString(R.string.dialog_finish_button)) { _, _ ->
                findNavController().navigateUp()
            }
        goBackConfirmationDialog.show()
    }

}