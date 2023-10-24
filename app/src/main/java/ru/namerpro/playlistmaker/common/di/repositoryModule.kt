package ru.namerpro.playlistmaker.common.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.namerpro.playlistmaker.common.data.FavouritesHistoryRepositoryImpl
import ru.namerpro.playlistmaker.common.data.converters.TracksDbConvertor
import ru.namerpro.playlistmaker.common.data.db.PlaylistApplicationDatabase
import ru.namerpro.playlistmaker.common.domain.api.FavouritesHistoryRepository
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
        TracksRepositoryImpl(get(), get())
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

    // Db

    single {
        Room.databaseBuilder(androidContext(), PlaylistApplicationDatabase::class.java, "database.db")
            .build()
    }

    factory {
        TracksDbConvertor()
    }

    single<FavouritesHistoryRepository> {
        FavouritesHistoryRepositoryImpl(get(), get())
    }

    // ===

}