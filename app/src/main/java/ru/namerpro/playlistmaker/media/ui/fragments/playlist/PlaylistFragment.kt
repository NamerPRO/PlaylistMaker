package ru.namerpro.playlistmaker.media.ui.fragments.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.adapter.PlaylistAdapter
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.state.PlaylistsStorgeState
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model.PlaylistFragmentViewModel

class PlaylistFragment : Fragment() {

    private var binding: FragmentPlaylistsBinding? = null

    private var playlistAdapter = PlaylistAdapter(ArrayList()) {
        // ToDo("Implement in next sprint")
    }

    private val viewModel: PlaylistFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding?.root
    }

    @Suppress("DEPRECATION")
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding?.playlistsList?.apply {
            adapter = playlistAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        binding?.playlistsCreate?.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment2)
        }

        viewModel.observePlaylistsStorageLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsStorgeState.Empty -> showEmptyStub()
                is PlaylistsStorgeState.WithPlaylists -> showPlaylists(state.playlists as ArrayList<PlaylistModel>)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getPlaylists()
    }

    private fun showEmptyStub() {
        binding?.noPlaylistsCreated?.isVisible = true
        binding?.playlistsList?.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showPlaylists(
        playlists: ArrayList<PlaylistModel>
    ) {
        binding?.noPlaylistsCreated?.isVisible = false
        binding?.playlistsList?.isVisible = true

        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}