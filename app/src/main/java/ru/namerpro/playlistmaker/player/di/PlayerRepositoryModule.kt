package ru.namerpro.playlistmaker.player.di

import org.koin.dsl.module
import ru.namerpro.playlistmaker.player.data.media_player.MediaPlayerRepositoryImpl
import ru.namerpro.playlistmaker.player.domain.api.MediaPlayerRepository

val playerRepositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl()
    }

}