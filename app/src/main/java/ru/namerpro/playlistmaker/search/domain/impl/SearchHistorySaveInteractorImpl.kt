package ru.namerpro.playlistmaker.search.domain.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.namerpro.playlistmaker.search.domain.api.SearchHistorySaveInteractor
import ru.namerpro.playlistmaker.search.domain.api.SearchHistorySaveRepository
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class SearchHistorySaveInteractorImpl(
    private val searchHistorySaveRepository: SearchHistorySaveRepository,
    private val gson: Gson
) : SearchHistorySaveInteractor {

    override fun saveTracks(
        tracks: ArrayList<TrackModel>
    ) {
        searchHistorySaveRepository.saveTracks(gson.toJson(tracks))
    }

    override fun loadTracks(): ArrayList<TrackModel> {
        val json = searchHistorySaveRepository.loadTracks()
        return gson.fromJson(json, object : TypeToken<ArrayList<TrackModel>>() {}.type) ?: ArrayList()
    }

    override fun getPreferences(): SharedPreferences {
        return searchHistorySaveRepository.getPreferences()
    }

}