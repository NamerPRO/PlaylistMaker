package ru.namerpro.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.namerpro.playlistmaker.databinding.FragmentFavouriteTracksBinding
import ru.namerpro.playlistmaker.media.ui.view_model.FavouriteTracksFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteTracksBinding

    private val viewModel: FavouriteTracksFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

}