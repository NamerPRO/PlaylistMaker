package ru.namerpro.playlistmaker.settings.di

import org.koin.dsl.module
import ru.namerpro.playlistmaker.settings.domain.api.NavigationInteractor
import ru.namerpro.playlistmaker.settings.domain.api.SharedPreferencesSettingsInteractor
import ru.namerpro.playlistmaker.settings.domain.impl.NavigationInteractorImpl
import ru.namerpro.playlistmaker.settings.domain.impl.SharedPreferencesSettingsInteractorImpl

val settingsInteractorModule = module {

    single<NavigationInteractor> {
        NavigationInteractorImpl(get())
    }

    single<SharedPreferencesSettingsInteractor> {
        SharedPreferencesSettingsInteractorImpl(get())
    }

}