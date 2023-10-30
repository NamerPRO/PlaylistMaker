package ru.namerpro.playlistmaker.media.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import ru.namerpro.playlistmaker.databinding.FragmentFavouriteTracksBinding
import ru.namerpro.playlistmaker.media.ui.view_model.FavouriteTracksFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.common.utils.debounce
import ru.namerpro.playlistmaker.media.ui.fragments.state.FavouritesState
import ru.namerpro.playlistmaker.player.ui.activity.PlayerActivity
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
        val audioIntent = Intent(requireContext(), PlayerActivity::class.java)
        audioIntent.putExtra(SearchViewModel.TRACK_INTENT_KEY, Gson().toJson(track))
        startActivity(audioIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)

        viewModel.observeFavouritesLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavouritesState.Empty -> showEmptyStub()
                is FavouritesState.Filled -> showFavouriteTracks(state.favouriteTracksList)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.fillFavourites()
    }

    private fun showEmptyStub() {
        binding.noPlaylists.visibility = View.VISIBLE
        binding.favouriteTracksList.visibility = View.GONE
    }

    private fun showFavouriteTracks(
        favourites: List<TrackModel>
    ) {
        trackAdapter = TrackAdapter(favourites as ArrayList<TrackModel>) { track ->
            clickDebounce(track)
        }

        binding.favouriteTracksList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }

        binding.noPlaylists.visibility = View.GONE
        binding.favouriteTracksList.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

}