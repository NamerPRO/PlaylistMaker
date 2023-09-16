package ru.namerpro.playlistmaker.root.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.namerpro.playlistmaker.root.ui.view_model.RootViewModel

val rootViewModelModule = module {

    viewModel {
        RootViewModel(get())
    }

}