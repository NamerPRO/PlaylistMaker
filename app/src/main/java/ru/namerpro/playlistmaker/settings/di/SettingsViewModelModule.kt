package ru.namerpro.playlistmaker.settings.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.namerpro.playlistmaker.settings.ui.view_model.SettingsViewModel

val settingsViewModelModule = module {

    viewModel {
        SettingsViewModel(get(), get())
    }

}