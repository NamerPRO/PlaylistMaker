package ru.namerpro.playlistmaker.common.di

import android.content.Intent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.namerpro.playlistmaker.media.ui.fragments.favourite_tracks.view_model.FavouriteTracksFragmentViewModel
import ru.namerpro.playlistmaker.media.ui.fragments.playlist.view_model.PlaylistFragmentViewModel
import ru.namerpro.playlistmaker.player.ui.view_model.PlayerViewModel
import ru.namerpro.playlistmaker.root.ui.view_model.RootViewModel
import ru.namerpro.playlistmaker.search.domain.model.TrackModel
import ru.namerpro.playlistmaker.search.ui.view_model.SearchViewModel
import ru.namerpro.playlistmaker.settings.ui.view_model.SettingsViewModel

val viewModelModule = module {

    // Root

    viewModel {
        RootViewModel(get())
    }

    // ===

    // Favourite tracks view model

    viewModel {
        FavouriteTracksFragmentViewModel(get())
    }

    // ===

    // Playlist view model

    viewModel {
        PlaylistFragmentViewModel(get())
    }

    // ===

    // Player

    viewModel { (track: TrackModel) ->
        PlayerViewModel(track, get(), get(), get(), get())
    }

    // ===

    // Search

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    // ===

    // Settings

    viewModel {
        SettingsViewModel(get(), get())
    }

    // ===

}