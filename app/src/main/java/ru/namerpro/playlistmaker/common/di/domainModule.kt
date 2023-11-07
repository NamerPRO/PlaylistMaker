package ru.namerpro.playlistmaker.common.di

import com.google.gson.Gson
import org.koin.dsl.module
import ru.namerpro.playlistmaker.common.data.db.TracksInPlaylistDatabase
import ru.namerpro.playlistmaker.common.data.db.TracksInPlaylistDatabase_Impl
import ru.namerpro.playlistmaker.common.domain.api.FavouritesDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseInteractor
import ru.namerpro.playlistmaker.common.domain.impl.FavouritesDatabaseInteractorImpl
import ru.namerpro.playlistmaker.common.domain.impl.PlaylistsDatabaseInteractorImpl
import ru.namerpro.playlistmaker.common.domain.impl.TracksInPlaylistDatabaseInteractorImpl
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.namerpro.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import ru.namerpro.playlistmaker.search.domain.api.SearchHistoryInteractor
import ru.namerpro.playlistmaker.search.domain.api.SearchHistorySaveInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import ru.namerpro.playlistmaker.search.domain.impl.SearchHistorySaveInteractorImpl
import ru.namerpro.playlistmaker.search.domain.impl.TracksInteractorImpl
import ru.namerpro.playlistmaker.settings.domain.api.NavigationInteractor
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsInteractor
import ru.namerpro.playlistmaker.settings.domain.impl.NavigationInteractorImpl
import ru.namerpro.playlistmaker.settings.domain.impl.SharedPreferencesSettingsInteractorImpl

val interactorModule = module {

    // Player

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    // ===

    // Search

    single<SearchHistorySaveInteractor> {
        SearchHistorySaveInteractorImpl(get(), get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(ArrayList(10))
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single {
        Gson()
    }

    // ===

    // Settings

    single<NavigationInteractor> {
        NavigationInteractorImpl(get())
    }

    single<SharedPreferencesSettingsInteractor> {
        SharedPreferencesSettingsInteractorImpl(get())
    }

    // ===

    // Favourite tracks

    single<FavouritesDatabaseInteractor> {
        FavouritesDatabaseInteractorImpl(get())
    }

    // ===

    // Playlists

    single<PlaylistsDatabaseInteractor> {
        PlaylistsDatabaseInteractorImpl(get())
    }

    single<TracksInPlaylistDatabaseInteractor> {
        TracksInPlaylistDatabaseInteractorImpl(get())
    }

    // ===

}