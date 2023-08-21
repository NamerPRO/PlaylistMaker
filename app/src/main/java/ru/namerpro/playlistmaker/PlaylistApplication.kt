package ru.namerpro.playlistmaker

import android.app.Application
import android.content.Context

class PlaylistApplication : Application() {

    companion object {
        var applicationContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        PlaylistApplication.applicationContext = applicationContext
    }

}