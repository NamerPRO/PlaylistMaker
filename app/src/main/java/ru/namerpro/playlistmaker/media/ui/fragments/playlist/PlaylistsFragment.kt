package ru.namerpro.playlistmaker.media.ui.fragments.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.common.utils.debounce
import ru.namerpro.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.namerpro.playlistmaker.media.domain.models.PlaylistModel
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.adapter.PlaylistAdapter
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.state.PlaylistsStorgeState
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model.PlaylistsFragmentViewModel
import ru.namerpro.playlistmaker.playlist.ui.fragment.PlaylistFragment
import ru.namerpro.playlistmaker.search.ui.fragment.SearchFragment

class PlaylistsFragment : Fragment() {

    private var binding: FragmentPlaylistsBinding? = null

    private val safeNavigation = debounce<Pair<Any?, Int>>(
        delayMillis = SearchFragment.CLICK_DEBOUNCE_DELAY,
        coroutineScope = lifecycleScope,
        useLastParam = false
    ) { state ->
        when (state.second) {
            R.id.action_mediaFragment_to_playlistFragment -> {
                findNavController().navigate(
                    R.id.action_mediaFragment_to_playlistFragment,
                    PlaylistFragment.createArgs(state.first as PlaylistModel)
                )
            }
            R.id.action_mediaFragment_to_createPlaylistFragment2 -> {
                findNavController().navigate(
                    R.id.action_mediaFragment_to_createPlaylistFragment2
                )
            }
        }
    }

    private var playlistAdapter = PlaylistAdapter(ArrayList()) { playlist ->
        safeNavigation(playlist to R.id.action_mediaFragment_to_playlistFragment)
    }

    private val viewModel: PlaylistsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding?.root
    }

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
            safeNavigation(Unit to R.id.action_mediaFragment_to_createPlaylistFragment2)
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
        fun newInstance() = PlaylistsFragment()
    }

}