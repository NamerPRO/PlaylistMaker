package ru.namerpro.playlistmaker.creator

import android.content.SharedPreferences
import ru.namerpro.playlistmaker.search.data.network.TracksRepositoryImpl
import ru.namerpro.playlistmaker.player.data.media_player.MediaPlayerRepositoryImpl
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerListener
import ru.namerpro.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerRepository
import ru.namerpro.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl
import ru.namerpro.playlistmaker.search.data.shared_preferences.SharedPreferencesSearchRepositoryImpl
import ru.namerpro.playlistmaker.search.domain.api.*
import ru.namerpro.playlistmaker.search.domain.impl.HistoryInteractorImpl
import ru.namerpro.playlistmaker.search.domain.impl.SharedPreferencesSearchInteractorImpl
import ru.namerpro.playlistmaker.search.domain.impl.TracksInteractorImpl
import ru.namerpro.playlistmaker.settings.domain.api.NavigationInteractor
import ru.namerpro.playlistmaker.settings.data.navigation.NavigationRepositoryImpl
import ru.namerpro.playlistmaker.settings.data.shared_preferences.SharedPreferencesSettingsRepositoryImpl
import ru.namerpro.playlistmaker.settings.domain.api.NavigationRepository
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsInteractor
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsRepository
import ru.namerpro.playlistmaker.settings.domain.impl.NavigationInteractorImpl
import ru.namerpro.playlistmaker.settings.domain.impl.SharedPreferencesSettingsInteractorImpl

object Creator {

    // Tracks

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient()
        )
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            repository = getTracksRepository()
        )
    }

    // ===

    // Media player

    private fun getMediaPlayerRepository(
        listener: MediaPlayerListener
    ): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(
            listener = listener
        )
    }

    fun provideMediaPlayerInteractor(
        listener: MediaPlayerListener
    ): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(
            mediaPlayerRepository = getMediaPlayerRepository(
                listener = listener
            )
        )
    }

    // ===

    // Search shared preferences

    private fun getSearchSharedPreferencesRepository(
        sharedPrefs: SharedPreferences
    ): SharedPreferencesSearchRepository {
        return SharedPreferencesSearchRepositoryImpl(
            sharedPrefs = sharedPrefs
        )
    }

    fun provideSearchSharedPreferencesInteractor(
        sharedPrefs: SharedPreferences
    ): SharedPreferencesSearchInteractor {
        return SharedPreferencesSearchInteractorImpl(
            sharedPreferencesSearchRepository =  getSearchSharedPreferencesRepository(
                sharedPrefs = sharedPrefs
            )
        )
    }

    // ===

    // History

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(
            trackHistory = ArrayList(10)
        )
    }

    // ===

    // Setting shared preferences

    private fun getSettingsSharedPreferencesRepository(
        sharedPrefs: SharedPreferences
    ): SharedPreferencesSettingsRepository{
        return SharedPreferencesSettingsRepositoryImpl(
            sharedPrefs = sharedPrefs
        )
    }

    fun provideSettingsSharedPreferencesInteractor(
        sharedPrefs: SharedPreferences
    ): SharedPreferencesSettingsInteractor {
        return SharedPreferencesSettingsInteractorImpl(
            sharedPreferencesSettingsRepository = getSettingsSharedPreferencesRepository(
                sharedPrefs = sharedPrefs
            )
        )
    }

    // ===

    // Settings intent navigator

    private fun getSettingsNavigationRepository(): NavigationRepository {
        return NavigationRepositoryImpl()
    }

    fun provideSettingsNavigatorInteractor(): NavigationInteractor {
        return NavigationInteractorImpl(
            navigationRepository = getSettingsNavigationRepository()
        )
    }

    // ===

}