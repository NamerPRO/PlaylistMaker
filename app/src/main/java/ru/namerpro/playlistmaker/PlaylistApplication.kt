package ru.namerpro.playlistmaker

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.namerpro.playlistmaker.common.di.interactorModule
import ru.namerpro.playlistmaker.common.di.repositoryModule
import ru.namerpro.playlistmaker.common.di.viewModelModule

class PlaylistApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PlaylistApplication.applicationContext = applicationContext

        startKoin {
            androidContext(this@PlaylistApplication)
            modules(interactorModule, repositoryModule, viewModelModule)
        }
    }

    companion object {
        var applicationContext: Context? = null
    }

}