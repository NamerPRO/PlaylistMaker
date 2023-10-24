package ru.namerpro.playlistmaker.search.domain.api

import android.content.SharedPreferences

interface SearchHistorySaveRepository {

    fun saveTracks(
        json: String
    )

    fun loadTracks() : String

    fun getPreferences() : SharedPreferences

}