package ru.namerpro.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.namerpro.playlistmaker.databinding.FragmentPlaylistsBinding
import ru.namerpro.playlistmaker.player.ui.view_model.PlayerViewModel

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding

    private val viewModel: PlayerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }

}