package ru.namerpro.playlistmaker.search.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.namerpro.playlistmaker.search.ui.view_model.SearchViewModel

val searchViewModelModule = module {

    viewModel {
        SearchViewModel(get(), get(), get())
    }

}