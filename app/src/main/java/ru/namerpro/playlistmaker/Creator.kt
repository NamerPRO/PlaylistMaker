package ru.namerpro.playlistmaker

import android.content.SharedPreferences
import ru.namerpro.playlistmaker.data.TracksRepositoryImpl
import ru.namerpro.playlistmaker.data.mediaplayer.MediaPlayerInteractorImpl
import ru.namerpro.playlistmaker.data.network.RetrofitNetworkClient
import ru.namerpro.playlistmaker.data.sharedprefs.SharedPreferencesInteractorImpl
import ru.namerpro.playlistmaker.domain.api.SharedPreferencesInteractor
import ru.namerpro.playlistmaker.presentation.presenters.MediaPlayerInteractor
import ru.namerpro.playlistmaker.presentation.presenters.MediaPlayerListener
import ru.namerpro.playlistmaker.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.domain.api.TracksRepository
import ru.namerpro.playlistmaker.domain.impl.TracksInteractorImpl
import ru.namerpro.playlistmaker.presentation.presenters.SharedPreferencesListener

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideMediaPlayerInteractor(listener: MediaPlayerListener): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(listener)
    }

    fun provideSharedPreferencesInteractor(sharedPrefs: SharedPreferences, listener: SharedPreferencesListener) : SharedPreferencesInteractor {
        return SharedPreferencesInteractorImpl(sharedPrefs, listener)
    }

}