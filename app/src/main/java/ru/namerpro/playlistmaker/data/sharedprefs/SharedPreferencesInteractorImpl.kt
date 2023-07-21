package ru.namerpro.playlistmaker.data.sharedprefs

import android.content.SharedPreferences
import ru.namerpro.playlistmaker.domain.api.SharedPreferencesInteractor
import ru.namerpro.playlistmaker.presentation.presenters.SharedPreferencesListener

class SharedPreferencesInteractorImpl(private val sharedPrefs: SharedPreferences, private val listener: SharedPreferencesListener) : SharedPreferencesInteractor {

    companion object {
        const val TRACK_HISTORY_PREFERENCES = "track_history_preferences"
        const val HISTORY_LIST_KEY = "history_list_key"
    }

    init {
        loadTracks()
    }

    private fun loadTracks() {
        val json = sharedPrefs.getString(HISTORY_LIST_KEY, null)
        listener.onPreferencesLoad(json ?: "")
    }

    override fun saveTracks() {
        val json = listener.onPreferencesSave()
        sharedPrefs.edit()
            .putString(HISTORY_LIST_KEY, json)
            .apply()
    }

    override fun getPreferences() : SharedPreferences {
        return sharedPrefs
    }

}