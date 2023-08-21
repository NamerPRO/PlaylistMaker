package ru.namerpro.playlistmaker.search.domain.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.namerpro.playlistmaker.search.domain.api.SharedPreferencesSearchInteractor
import ru.namerpro.playlistmaker.search.domain.api.SharedPreferencesSearchRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class SharedPreferencesSearchInteractorImpl(
    private val sharedPreferencesSearchRepository: SharedPreferencesSearchRepository
) : SharedPreferencesSearchInteractor {

    override fun saveTracks(tracks: ArrayList<TrackModel>) {
        sharedPreferencesSearchRepository.saveTracks(Gson().toJson(tracks))
    }

    override fun loadTracks(): ArrayList<TrackModel> {
        val json = sharedPreferencesSearchRepository.loadTracks()
        return Gson().fromJson(json, object : TypeToken<ArrayList<TrackModel>>() {}.type)
    }

    override fun getPreferences(): SharedPreferences {
        return sharedPreferencesSearchRepository.getPreferences()
    }

}