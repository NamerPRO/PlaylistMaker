package ru.namerpro.playlistmaker.main.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.namerpro.playlistmaker.main.ui.view_model.MainViewModel

val mainViewModelModule = module {

    viewModel {
        MainViewModel(get())
    }

}