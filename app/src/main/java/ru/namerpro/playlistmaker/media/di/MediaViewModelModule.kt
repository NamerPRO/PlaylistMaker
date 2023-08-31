package ru.namerpro.playlistmaker.media.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.namerpro.playlistmaker.media.ui.view_model.FavouriteTracksFragmentViewModel
import ru.namerpro.playlistmaker.media.ui.view_model.PlaylistFragmentViewModel

val mediaViewModelModule = module {

    viewModel {
        FavouriteTracksFragmentViewModel()
    }

    viewModel {
        PlaylistFragmentViewModel()
    }

}