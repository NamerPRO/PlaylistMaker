package ru.namerpro.playlistmaker.search.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.namerpro.playlistmaker.search.data.network.TracksRepositoryImpl
import ru.namerpro.playlistmaker.search.data.shared_preferences.SharedPreferencesSearchRepositoryImpl
import ru.namerpro.playlistmaker.search.domain.api.SharedPreferencesSearchRepository
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository

val searchRepositoryModule = module {

    single {
        androidContext()
            .getSharedPreferences(
                SharedPreferencesSearchRepositoryImpl.TRACK_HISTORY_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    single<SharedPreferencesSearchRepository> {
        SharedPreferencesSearchRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

}