package ru.namerpro.playlistmaker.player.di

import android.content.Intent
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.namerpro.playlistmaker.player.ui.view_model.PlayerViewModel

val playerViewModelModule = module {

    viewModel { (intent: Intent) ->
        PlayerViewModel(intent, get())
    }

}