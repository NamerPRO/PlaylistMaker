package ru.namerpro.playlistmaker.domain.api

import android.content.SharedPreferences

interface SharedPreferencesInteractor {

    fun saveTracks()
    fun getPreferences() : SharedPreferences

}