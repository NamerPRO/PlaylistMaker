package ru.namerpro.playlistmaker.media.ui.fragments.playlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.state.PlaylistsAddingState
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model.PlaylistFragmentViewModel

class CreatePlaylistFragment : Fragment() {

    private val viewModel: PlaylistFragmentViewModel by viewModel()

    private var binding: FragmentNewPlaylistBinding? = null

//    private var pickedPlaylistCoverUri: Uri? = null
    private var wasImageSelected: Boolean = false

//    override fun onSaveInstanceState(
//        outState: Bundle
//    ) {
//        super.onSaveInstanceState(outState)
//        Log.e("123", pickedPlaylistCoverUri?.path.toString())
//        outState.putString(PICKED_IMAGE_URI_KEY, Gson().toJson(pickedPlaylistCoverUri?.path))
//    }

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
                viewModel.pickedPlaylistCoverUri = uri
                binding?.newPlaylistSelectCover?.scaleType = ImageView.ScaleType.CENTER_CROP
                binding?.newPlaylistSelectCover?.setImageURI(uri)
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
                            state.playlistTitle
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

        wasImageSelected = viewModel.pickedPlaylistCoverUri != null

        if (wasImageSelected) {
            binding?.newPlaylistSelectCover?.scaleType = ImageView.ScaleType.CENTER_CROP
            binding?.newPlaylistSelectCover?.setImageURI(viewModel.pickedPlaylistCoverUri)
        }
    }

    private fun onFragmentCloseLogic() {
        if (binding?.newPlaylistPlaylistTitleEdittext?.text?.isEmpty() != false
                && binding?.newPlaylistPlaylistDescriptionEdittext?.text?.isEmpty() != false
                && !wasImageSelected) {
            findNavController().navigateUp()
            return
        }
        val goBackConfirmationDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут удалены.")
            .setNeutralButton("Отмена") { _, _ -> }
            .setNegativeButton("Завершить") { _, _ ->
                findNavController().navigateUp()
            }
        goBackConfirmationDialog.show()
    }

    companion object {
        private const val PICKED_IMAGE_URI_KEY = "picked_image_uri_key"
    }

}