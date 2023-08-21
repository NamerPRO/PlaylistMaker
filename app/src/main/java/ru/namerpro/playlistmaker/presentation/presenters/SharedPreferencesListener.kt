package ru.namerpro.playlistmaker.presentation.presenters

interface SharedPreferencesListener {

    fun onPreferencesLoad(json: String)
    fun onPreferencesSave(): String


}