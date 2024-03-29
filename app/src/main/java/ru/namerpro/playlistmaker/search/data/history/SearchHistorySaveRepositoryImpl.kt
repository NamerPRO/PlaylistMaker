package ru.namerpro.playlistmaker.search.data.history

import android.content.SharedPreferences
import ru.namerpro.playlistmaker.search.domain.api.SearchHistorySaveRepository

class SearchHistorySaveRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : SearchHistorySaveRepository {

    override fun loadTracks(): String {
        val json = sharedPrefs.getString(HISTORY_LIST_KEY, null)
        return json ?: ""
    }

    override fun saveTracks(
        json: String
    ) {
        sharedPrefs.edit()
            .putString(HISTORY_LIST_KEY, json)
            .apply()
    }

    override fun getPreferences() : SharedPreferences {
        return sharedPrefs
    }

    companion object {
        const val TRACK_HISTORY_PREFERENCES = "track_history_preferences"
        const val HISTORY_LIST_KEY = "history_list_key"
    }

}