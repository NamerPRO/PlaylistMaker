package ru.namerpro.playlistmaker.settings.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.namerpro.playlistmaker.settings.data.navigation.NavigationRepositoryImpl
import ru.namerpro.playlistmaker.settings.data.shared_preferences.SharedPreferencesSettingsRepositoryImpl
import ru.namerpro.playlistmaker.settings.domain.api.NavigationRepository
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsRepository

val settingsRepositoryModule = module {

    single {
        androidContext()
            .getSharedPreferences(
                SharedPreferencesSettingsRepositoryImpl.SWITCH_POSITION_PREFERENCES,
                Context.MODE_PRIVATE
            )
    }

    single<SharedPreferencesSettingsRepository> {
        SharedPreferencesSettingsRepositoryImpl(get())
    }

    single<NavigationRepository> {
        NavigationRepositoryImpl()
    }

}