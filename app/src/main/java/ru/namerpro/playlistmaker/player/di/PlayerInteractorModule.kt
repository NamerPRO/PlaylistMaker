package ru.namerpro.playlistmaker.player.di

import org.koin.dsl.module
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.namerpro.playlistmaker.player.domain.impl.MediaPlayerInteractorImpl

val playerInteractorModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

}