package ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import ru.namerpro.playlistmaker.databinding.FragmentFavouriteTracksBinding
import ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks.view_model.FavouriteTracksFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.R
import ru.namerpro.playlistmaker.common.utils.debounce
import ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks.state.FavouritesState
import ru.namerpro.playlistmaker.player.ui.fragment.PlayerFragment
import ru.namerpro.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.adapter.TrackAdapter
import ru.namerpro.playlistmaker.search.ui.fragments.SearchFragment
import ru.namerpro.playlistmaker.search.ui.view_model.SearchViewModel

class FavouriteTracksFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteTracksBinding

    private val viewModel: FavouriteTracksFragmentViewModel by viewModel()

    private lateinit var trackAdapter: TrackAdapter

    private val clickDebounce = debounce<TrackModel>(
        delayMillis = SearchFragment.CLICK_DEBOUNCE_DELAY,
        coroutineScope = lifecycleScope,
        useLastParam = false
    ) { track ->
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            PlayerViewModel.createArgs(Gson().toJson(track))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeFavouritesLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavouritesState.Empty -> showEmptyStub()
                is FavouritesState.Filled -> showFavouriteTracks(state.favouriteTracksList)
            }
        }

        trackAdapter = TrackAdapter(ArrayList()) { track ->
            clickDebounce(track)
        }

        binding.favouriteTracksList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fillFavourites()
    }

    private fun showEmptyStub() {
        binding.noPlaylists.visibility = View.VISIBLE
        binding.favouriteTracksList.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showFavouriteTracks(
        favourites: List<TrackModel>
    ) {
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(favourites)
        trackAdapter.notifyDataSetChanged()

        binding.noPlaylists.visibility = View.GONE
        binding.favouriteTracksList.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

}