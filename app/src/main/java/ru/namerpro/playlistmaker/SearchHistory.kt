package ru.namerpro.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACK_HISTORY_PREFERENCES = "track_history_preferences"
const val HISTORY_LIST_KEY = "history_list_key"

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    companion object {
        val trackHistory = mutableListOf<Track>()
        const val maximumElementsInHistory = 10
    }

    init {
        loadTracks()
    }

    private fun loadTracks() {
        val json = sharedPrefs.getString(HISTORY_LIST_KEY, null) ?: return
        trackHistory.addAll(Gson().fromJson<MutableList<Track>>(json, object : TypeToken<MutableList<Track>>() {}.type))
    }

    fun saveTracks() {
        val json = Gson().toJson(trackHistory)
        sharedPrefs.edit()
            .putString(HISTORY_LIST_KEY, json)
            .apply()
        trackHistory.clear()
    }

    fun isHistoryEmpty() : Boolean {
        return trackHistory.isEmpty()
    }

    fun addTrack(track: Track) {
        val elementToDelete = trackHistory.find{ it.trackId == track.trackId }
        if (elementToDelete != null) {
            trackHistory.remove(elementToDelete)
        } else if (trackHistory.size == maximumElementsInHistory) {
            trackHistory.removeLast()
        }
        trackHistory.add(0, track)
    }

    fun clearHistory() {
        trackHistory.clear()
    }

}