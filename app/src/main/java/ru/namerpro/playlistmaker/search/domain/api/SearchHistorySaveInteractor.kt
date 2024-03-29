package ru.namerpro.playlistmaker.search.domain.api

import android.content.SharedPreferences
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

interface SearchHistorySaveInteractor {

    fun saveTracks(
        tracks: ArrayList<TrackModel>
    )

    fun loadTracks() : ArrayList<TrackModel>

    fun getPreferences() : SharedPreferences

}