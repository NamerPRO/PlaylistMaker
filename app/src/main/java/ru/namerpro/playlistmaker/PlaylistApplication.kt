package ru.namerpro.playlistmaker

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.namerpro.playlistmaker.media.di.mediaViewModelModule
import ru.namerpro.playlistmaker.player.di.playerInteractorModule
import ru.namerpro.playlistmaker.player.di.playerRepositoryModule
import ru.namerpro.playlistmaker.player.di.playerViewModelModule
import ru.namerpro.playlistmaker.root.di.rootViewModelModule
import ru.namerpro.playlistmaker.search.di.searchInteractorModule
import ru.namerpro.playlistmaker.search.di.searchNetworkModule
import ru.namerpro.playlistmaker.search.di.searchRepositoryModule
import ru.namerpro.playlistmaker.search.di.searchViewModelModule
import ru.namerpro.playlistmaker.settings.di.settingsInteractorModule
import ru.namerpro.playlistmaker.settings.di.settingsRepositoryModule
import ru.namerpro.playlistmaker.settings.di.settingsViewModelModule

class PlaylistApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PlaylistApplication.applicationContext = applicationContext

        startKoin {
            androidContext(this@PlaylistApplication)
            modules(
                searchInteractorModule, searchNetworkModule, searchRepositoryModule, searchViewModelModule,
                settingsInteractorModule, settingsRepositoryModule, settingsViewModelModule,
                playerInteractorModule, playerRepositoryModule, playerViewModelModule,
                rootViewModelModule, mediaViewModelModule
            )
        }
    }

    companion object {
        var applicationContext: Context? = null
    }

}