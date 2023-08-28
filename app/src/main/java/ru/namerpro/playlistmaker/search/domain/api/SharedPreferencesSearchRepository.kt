package ru.namerpro.playlistmaker.search.domain.api

import android.content.SharedPreferences

interface SharedPreferencesSearchRepository {

    fun saveTracks(
        json: String
    )

    fun loadTracks() : String

    fun getPreferences() : SharedPreferences

}