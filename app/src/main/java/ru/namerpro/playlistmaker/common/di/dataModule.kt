package ru.namerpro.playlistmaker.common.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.namerpro.playlistmaker.common.data.FavouritesDatabaseRepositoryImpl
import ru.namerpro.playlistmaker.common.data.PlaylistsDatabaseRepositoryImpl
import ru.namerpro.playlistmaker.common.data.TracksInPlaylistDatabaseRepositoryImpl
import ru.namerpro.playlistmaker.common.data.converters.PlaylistsDbConvertor
import ru.namerpro.playlistmaker.common.data.converters.TrackInPlaylistDbConvertor
import ru.namerpro.playlistmaker.common.data.converters.TracksDbConvertor
import ru.namerpro.playlistmaker.common.data.db.FavouriteTracksDatabase
import ru.namerpro.playlistmaker.common.data.db.PlaylistsDatabase
import ru.namerpro.playlistmaker.common.data.db.TracksInPlaylistDatabase
import ru.namerpro.playlistmaker.common.domain.api.FavouritesDatabaseRepository
import ru.namerpro.playlistmaker.common.domain.api.PlaylistsDatabaseRepository
import ru.namerpro.playlistmaker.common.domain.api.TracksInPlaylistDatabaseRepository
import ru.namerpro.playlistmaker.player.data.media_player.MediaPlayerRepositoryImpl
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerRepository
import ru.namerpro.playlistmaker.search.data.network.ItunesServiceApi
import ru.namerpro.playlistmaker.search.data.network.NetworkClient
import ru.namerpro.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.namerpro.playlistmaker.search.data.network.TracksRepositoryImpl
import ru.namerpro.playlistmaker.search.data.history.SearchHistorySaveRepositoryImpl
import ru.namerpro.playlistmaker.search.domain.api.SearchHistorySaveRepository
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository
import ru.namerpro.playlistmaker.settings.data.navigation.NavigationRepositoryImpl
import ru.namerpro.playlistmaker.settings.data.shared_preferences.SharedPreferencesSettingsRepositoryImpl
import ru.namerpro.playlistmaker.settings.domain.api.NavigationRepository
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsRepository

val repositoryModule = module {

    // Player

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl()
    }

    // ===

    // Search

    single {
        androidContext()
            .getSharedPreferences(
                SearchHistorySaveRepositoryImpl.TRACK_HISTORY_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    single<SearchHistorySaveRepository> {
        SearchHistorySaveRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ItunesServiceApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesServiceApi::class.java)
    }

    // ===

    // Settings

    single<SharedPreferencesSettingsRepository> {
        SharedPreferencesSettingsRepositoryImpl(get())
    }

    single<NavigationRepository> {
        NavigationRepositoryImpl()
    }

    // ===

    // Favourite tacks

    single {
        Room.databaseBuilder(androidContext(), FavouriteTracksDatabase::class.java, "favourite_tracks.db")
            .build()
    }

    factory {
        TracksDbConvertor()
    }

    single<FavouritesDatabaseRepository> {
        FavouritesDatabaseRepositoryImpl(get(), get())
    }

    // ===

    // Playlists

    single {
        Room.databaseBuilder(androidContext(), PlaylistsDatabase::class.java, "playlists.db")
            .build()
    }

    factory {
        PlaylistsDbConvertor()
    }

    single<PlaylistsDatabaseRepository> {
        PlaylistsDatabaseRepositoryImpl(get(), get())
    }

    single {
        Room.databaseBuilder(androidContext(), TracksInPlaylistDatabase::class.java, "tracks_in_playlist.db")
            .build()
    }

    factory {
        TrackInPlaylistDbConvertor()
    }

    single<TracksInPlaylistDatabaseRepository> {
        TracksInPlaylistDatabaseRepositoryImpl(get(), get())
    }

    // ===

}